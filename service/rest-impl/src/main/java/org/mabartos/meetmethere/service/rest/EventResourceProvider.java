package org.mabartos.meetmethere.service.rest;

import io.smallrye.mutiny.Uni;
import org.mabartos.meetmethere.api.service.EventService;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;
import org.mabartos.meetmethere.interaction.rest.api.EventInvitationsResource;
import org.mabartos.meetmethere.interaction.rest.api.EventResource;
import org.mabartos.meetmethere.interaction.rest.api.model.EventJson;
import org.mabartos.meetmethere.interaction.rest.api.model.mapper.EventJsonDomainMapper;
import org.mapstruct.factory.Mappers;

import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.mabartos.meetmethere.service.rest.EventsResourceProvider.getSingleEvent;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class EventResourceProvider implements EventResource {
    private final EventJsonDomainMapper mapper = Mappers.getMapper(EventJsonDomainMapper.class);
    private final MeetMeThereSession session;
    private final Long eventId;

    public EventResourceProvider(MeetMeThereSession session, Long id) {
        this.session = session;
        this.eventId = id;
    }

    @GET
    public Uni<EventJson> getEvent() {
        return getSingleEvent(session.eventBus(), EventService.EVENT_GET_EVENT_EVENT, eventId);
    }

    @DELETE
    public Response removeEvent() {
        session.eventBus().publish(EventService.EVENT_REMOVE_EVENT, eventId);
        return Response.ok().build();
    }

    @PATCH
    public Uni<EventJson> updateEvent(EventJson event) {
        if (event.getId() != null && !eventId.equals(event.getId())) {
            throw new BadRequestException("Cannot update Event - different IDs");
        }
        event.setId(eventId);

        return getSingleEvent(session.eventBus(), EventService.EVENT_UPDATE_EVENT, mapper.toDomain(event));
    }

    @Path("/invitations")
    public EventInvitationsResource getInvitations() {
        return new EventInvitationsResourceProvider(session, eventId);
    }
}
