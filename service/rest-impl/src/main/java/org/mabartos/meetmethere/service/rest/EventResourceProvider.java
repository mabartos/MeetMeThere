package org.mabartos.meetmethere.service.rest;

import io.smallrye.mutiny.Uni;
import org.mabartos.meetmethere.dto.Event;
import org.mabartos.meetmethere.interaction.rest.api.EventInvitationsResource;
import org.mabartos.meetmethere.interaction.rest.api.EventResource;
import org.mabartos.meetmethere.model.EventModel;
import org.mabartos.meetmethere.model.exception.ModelNotFoundException;
import org.mabartos.meetmethere.session.MeetMeThereSession;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PATCH;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.mabartos.meetmethere.DtoToModel.updateModel;
import static org.mabartos.meetmethere.ModelToDto.toDto;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class EventResourceProvider implements EventResource {
    private final MeetMeThereSession session;
    private final EventModel event;

    public EventResourceProvider(MeetMeThereSession session, EventModel event) {
        this.session = session;
        if (event == null) throw new NotFoundException("Cannot find event");
        this.event = event;
    }

    @GET
    public Uni<Event> getEvent() {
        return Uni.createFrom().item(toDto(event));
    }

    @DELETE
    public Response removeEvent() {
        try {
            session.events().removeEvent(event.getId());
            return Response.noContent().build();
        } catch (ModelNotFoundException e) {
            throw new NotFoundException("Cannot remove event. Event doesn't exist.");
        }
    }

    @PATCH
    public Uni<Event> updateEvent(Event event) {
        try {
            updateModel(event, this.event);
            session.events().updateEvent(this.event);
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("Cannot update event. Event doesn't exist.");
        }

        return Uni.createFrom()
                .item(toDto(session.events().getEventById(this.event.getId())));
    }

    @Path("/invitations")
    public EventInvitationsResource getInvitations() {
        return new EventInvitationsResourceProvider(session, event);
    }
}
