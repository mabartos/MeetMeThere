package org.mabartos.meetmethere.service.rest;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.mabartos.meetmethere.ModelToDto;
import org.mabartos.meetmethere.dto.Event;
import org.mabartos.meetmethere.interaction.rest.api.EventResource;
import org.mabartos.meetmethere.interaction.rest.api.EventsResource;
import org.mabartos.meetmethere.model.Coordinates;
import org.mabartos.meetmethere.model.EventModel;
import org.mabartos.meetmethere.session.MeetMeThereSession;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Set;

import static org.mabartos.meetmethere.DtoToModel.updateModel;
import static org.mabartos.meetmethere.ModelToDto.toDto;
import static org.mabartos.meetmethere.interaction.rest.api.ResourceConstants.FIRST_RESULT;
import static org.mabartos.meetmethere.interaction.rest.api.ResourceConstants.ID;
import static org.mabartos.meetmethere.interaction.rest.api.ResourceConstants.MAX_RESULTS;

@Path("/events")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class EventsResourceProvider implements EventsResource {

    @Context
    MeetMeThereSession session;

    @GET
    public Multi<Event> getEvents(@QueryParam(FIRST_RESULT) int firstResult, @QueryParam(MAX_RESULTS) int maxResults) {
        return Multi.createFrom()
                .items(session.events()
                        .getEvents(firstResult, maxResults)
                        .stream()
                        .map(ModelToDto::toDto)
                        .distinct()
                        .toArray())
                .onItem()
                .castTo(Event.class);
    }

    @GET
    @Path("/{id}")
    public EventResource getEventById(@PathParam(ID) Long id) {
        final EventModel event = session.events().getEventById(id);

        if (event == null) {
            throw new NotFoundException("Cannot find event with id: " + id);
        }

        return new EventResourceProvider(session, event);
    }

    @GET
    @Path("/{title}")
    public Multi<Event> searchEventsByTitle(@PathParam("title") String title) {
        final Set<EventModel> events = session.events().searchByTitle(title);

        return Multi.createFrom()
                .items(events.stream().map(ModelToDto::toDto))
                .onItem()
                .castTo(Event.class);
    }

    @GET
    @Path("/coordinates")
    public Multi<Event> searchEventsByCoordinates(Coordinates coordinates) {
        final Set<EventModel> events = session.events().searchByCoordinates(coordinates);

        return Multi.createFrom()
                .items(events.stream().map(ModelToDto::toDto))
                .onItem()
                .castTo(Event.class);
    }

    @POST
    public Uni<Event> createEvent(Event event) {
        if (session.events().getEventById(event.getId()) != null) {
            throw new BadRequestException("Event already exists.");
        }

        EventModel model = session.events().createEvent(event.getTitle());
        updateModel(event, model);

        session.events().updateEvent(model);
        return Uni.createFrom().item(toDto(session.events().getEventById(model.getId())));
    }

    @GET
    @Path("/count")
    public Uni<Long> getEventsCount() {
        return Uni.createFrom().item(session.events().getEventsCount());
    }
}
