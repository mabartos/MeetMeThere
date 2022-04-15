package org.mabartos.meetmethere.service.rest;

import io.smallrye.mutiny.Uni;
import org.mabartos.meetmethere.dto.Event;
import org.mabartos.meetmethere.interaction.rest.api.EventResource;
import org.mabartos.meetmethere.model.EventModel;
import org.mabartos.meetmethere.model.exception.ModelNotFoundException;
import org.mabartos.meetmethere.session.MeetMeThereSession;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PATCH;
import javax.ws.rs.core.Response;

import static org.mabartos.meetmethere.DtoToModel.updateModel;
import static org.mabartos.meetmethere.ModelToDto.toDto;

public class EventResourceProvider implements EventResource {
    private final MeetMeThereSession session;
    private final EventModel event;

    public EventResourceProvider(MeetMeThereSession session, EventModel event) {
        this.session = session;
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
}
