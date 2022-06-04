package org.mabartos.meetmethere.service.rest;

import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.mutiny.core.eventbus.Message;
import org.mabartos.meetmethere.api.domain.EventInvitation;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;
import org.mabartos.meetmethere.interaction.rest.api.EventInvitationResource;
import org.mabartos.meetmethere.interaction.rest.api.EventInvitationsResource;
import org.mabartos.meetmethere.interaction.rest.api.model.EventInvitationJson;
import org.mabartos.meetmethere.interaction.rest.api.model.mapper.EventInvitationJsonDomainMapper;
import org.mabartos.meetmethere.service.rest.util.EventBusUtil;
import org.mapstruct.factory.Mappers;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

import static org.mabartos.meetmethere.api.model.eventbus.invitation.InvitationEventsNames.EVENT_INVITE_COUNT_EVENT;
import static org.mabartos.meetmethere.api.model.eventbus.invitation.InvitationEventsNames.EVENT_INVITE_CREATE_EVENT;
import static org.mabartos.meetmethere.api.model.eventbus.invitation.InvitationEventsNames.EVENT_INVITE_GET_MULTIPLE_EVENT;
import static org.mabartos.meetmethere.api.model.eventbus.invitation.InvitationEventsNames.EVENT_INVITE_REMOVE_MULTIPLE_EVENT;
import static org.mabartos.meetmethere.interaction.rest.api.ResourceConstants.ID;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Transactional
@Authenticated
public class EventInvitationsResourceProvider implements EventInvitationsResource {
    static final String CACHE_NAME = "event-invitation-resource-provider-cache";

    private static final EventInvitationJsonDomainMapper mapper = Mappers.getMapper(EventInvitationJsonDomainMapper.class);
    private final MeetMeThereSession session;
    private final Long eventId;

    public EventInvitationsResourceProvider(MeetMeThereSession session, Long eventId) {
        this.session = session;
        this.eventId = eventId;
    }

    @GET
    @CacheResult(cacheName = CACHE_NAME)
    public Uni<Set<EventInvitationJson>> getInvitations() {
        session.auth().events().invitations().canView();

        return getSetOfEventInvitations(session.eventBus(), EVENT_INVITE_GET_MULTIPLE_EVENT, eventId);
    }

    @POST
    public Uni<Long> createInvitation(EventInvitationJson invitation) {
        session.auth().events().invitations().requireManage(invitation);

        return EventBusUtil.createEntity(session.eventBus(), EVENT_INVITE_CREATE_EVENT, mapper.toDomain(invitation));
    }

    @DELETE
    @CacheInvalidateAll(cacheName = CACHE_NAME)
    public Response removeInvitations() {
        session.auth().events().requireManageId(eventId);

        session.eventBus().publish(EVENT_INVITE_REMOVE_MULTIPLE_EVENT, eventId);
        return Response.ok().build();
    }

    @Path("/{id}")
    @CacheResult(cacheName = CACHE_NAME)
    public EventInvitationResource getInvitationById(@PathParam(ID) Long id) {
        return new EventInvitationResourceProvider(session, id);
    }

    @GET
    @Path("/count")
    @CacheResult(cacheName = CACHE_NAME)
    public Uni<Long> getInvitationsCount() {
        session.auth().events().requireViewId(eventId);

        return session.eventBus()
                .<Long>request(EVENT_INVITE_COUNT_EVENT, eventId)
                .onItem()
                .transform(Message::body);
    }

    protected static Uni<EventInvitationJson> getSingleEventInvitation(EventBus bus, String address, Object object) {
        return EventBusUtil.<EventInvitation>getSingleEntity(bus, address, object).map(mapper::toJson);
    }

    protected static Uni<Set<EventInvitationJson>> getSetOfEventInvitations(EventBus bus, String address, Object object) {
        return EventBusUtil.getSetOfEntities(bus, address, object, mapper::toJson);
    }
}
