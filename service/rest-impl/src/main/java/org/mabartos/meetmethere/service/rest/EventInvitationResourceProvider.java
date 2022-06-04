package org.mabartos.meetmethere.service.rest;

import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheResult;
import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;
import org.mabartos.meetmethere.interaction.rest.api.EventInvitationResource;
import org.mabartos.meetmethere.interaction.rest.api.model.EventInvitationJson;
import org.mabartos.meetmethere.interaction.rest.api.model.mapper.EventInvitationJsonDomainMapper;
import org.mapstruct.factory.Mappers;

import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.mabartos.meetmethere.api.model.eventbus.invitation.InvitationEventsNames.EVENT_INVITE_GET_SINGLE_EVENT;
import static org.mabartos.meetmethere.api.model.eventbus.invitation.InvitationEventsNames.EVENT_INVITE_REMOVE_SINGLE_EVENT;
import static org.mabartos.meetmethere.api.model.eventbus.invitation.InvitationEventsNames.EVENT_INVITE_UPDATE_EVENT;
import static org.mabartos.meetmethere.service.rest.EventInvitationsResourceProvider.getSingleEventInvitation;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Transactional
@Authenticated
public class EventInvitationResourceProvider implements EventInvitationResource {
    private static final EventInvitationJsonDomainMapper mapper = Mappers.getMapper(EventInvitationJsonDomainMapper.class);
    private final MeetMeThereSession session;
    private final Long invitationId;

    public EventInvitationResourceProvider(MeetMeThereSession session, Long invitationId) {
        this.session = session;
        this.invitationId = invitationId;
        session.context().setCurrentInvitation(invitationId);
    }

    @GET
    @CacheResult(cacheName = EventInvitationsResourceProvider.CACHE_NAME)
    public Uni<EventInvitationJson> getInvitation() {
        session.auth().events().invitations().requireViewId(invitationId);

        return getSingleEventInvitation(session.eventBus(), EVENT_INVITE_GET_SINGLE_EVENT, invitationId);
    }

    @DELETE
    public Response removeInvitation() {
        session.auth().events().invitations().requireManageId(invitationId);

        session.eventBus().publish(EVENT_INVITE_REMOVE_SINGLE_EVENT, invitationId);
        invalidateById(invitationId);
        return Response.ok().build();
    }

    @PATCH
    public Uni<EventInvitationJson> updateInvitation(EventInvitationJson invitation) {
        if (!invitationId.equals(invitation.getEventId())) {
            throw new BadRequestException("Cannot update invitation. Different Invitation IDs.");
        }

        session.auth().events().invitations().requireManageId(invitationId);

        invalidateById(invitationId);
        return getSingleEventInvitation(session.eventBus(), EVENT_INVITE_UPDATE_EVENT, mapper.toDomain(invitation));
    }

    @CacheInvalidate(cacheName = EventInvitationsResourceProvider.CACHE_NAME)
    void invalidateById(Long id) {
    }
}
