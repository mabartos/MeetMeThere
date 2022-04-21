package org.mabartos.meetmethere.service.rest;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.mabartos.meetmethere.interaction.rest.api.model.EventJson;
import org.mabartos.meetmethere.interaction.rest.api.model.ModelToJson;
import org.mabartos.meetmethere.api.domain.Event;
import org.mabartos.meetmethere.interaction.rest.api.EventResource;
import org.mabartos.meetmethere.interaction.rest.api.EventsResource;
import org.mabartos.meetmethere.api.model.Coordinates;
import org.mabartos.meetmethere.api.model.EventModel;
import org.mabartos.meetmethere.api.model.UserModel;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;

import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Set;

import static org.mabartos.meetmethere.interaction.rest.api.model.JsonToModel.updateModel;
import static org.mabartos.meetmethere.interaction.rest.api.model.ModelToJson.toJson;
import static org.mabartos.meetmethere.interaction.rest.api.ResourceConstants.FIRST_RESULT;
import static org.mabartos.meetmethere.interaction.rest.api.ResourceConstants.ID;
import static org.mabartos.meetmethere.interaction.rest.api.ResourceConstants.MAX_RESULTS;

@Path("/events")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
@Transactional
public class EventsResourceProvider implements EventsResource {

    @Context
    MeetMeThereSession session;

    @GET
    public Multi<EventJson> getEvents(@QueryParam(FIRST_RESULT) int firstResult, @QueryParam(MAX_RESULTS) int maxResults) {
        return Multi.createFrom()
                .items(session.events()
                        .getEvents(firstResult, maxResults)
                        .stream()
                        .map(ModelToJson::toJson)
                        .distinct()
                        .toArray())
                .onItem()
                .castTo(Event.class);
    }

    @Path("/{id}")
    public EventResource getEventById(@PathParam(ID) Long id) {
        return new EventResourceProvider(session, session.events().getEventById(id));
    }

    @GET
    @Path("/{title}")
    public Multi<EventJson> searchEventsByTitle(@PathParam("title") String title) {
        final Set<EventModel> events = session.events().searchByTitle(title);

        return Multi.createFrom()
                .items(events.stream().map(ModelToJson::toJson))
                .onItem()
                .castTo(Event.class);
    }

    @GET
    @Path("/coordinates")
    public Multi<EventJson> searchEventsByCoordinates(@QueryParam("longitude") Double longitude,
                                                  @QueryParam("latitude") Double latitude) {
        if (longitude == null || latitude == null) {
            throw new BadRequestException("You need to specify both longitude and latitude.");
        }

        final Set<EventModel> events = session.events().searchByCoordinates(new Coordinates(longitude, latitude));

        return Multi.createFrom()
                .items(events.stream().map(ModelToJson::toJson))
                .onItem()
                .castTo(Event.class);
    }

    @POST
    public Uni<EventJson> createEvent(EventJson event) {
        if (event.getId() != null && session.events().getEventById(event.getId()) != null) {
            throw new BadRequestException("Event already exists with the id.");
        }

        final UserModel creator = session.users().getUserById(event.getCreatedById());

        EventModel model = session.events().createEvent(event.getTitle(), creator);
        updateModel(event, model);

        return Uni.createFrom().item(ModelToJson.toJson(session.events().updateEvent(model)));
    }

    @GET
    @Path("/count")
    public Uni<Long> getEventsCount() {
        return Uni.createFrom().item(session.events().getEventsCount());
    }
}
