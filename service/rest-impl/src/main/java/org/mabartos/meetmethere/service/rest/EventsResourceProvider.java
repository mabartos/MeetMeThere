package org.mabartos.meetmethere.service.rest;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.mutiny.core.eventbus.Message;
import org.mabartos.meetmethere.api.codecs.SetHolder;
import org.mabartos.meetmethere.api.model.Coordinates;
import org.mabartos.meetmethere.api.model.EventModel;
import org.mabartos.meetmethere.api.model.eventbus.PaginationObject;
import org.mabartos.meetmethere.api.service.EventService;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;
import org.mabartos.meetmethere.interaction.rest.api.EventResource;
import org.mabartos.meetmethere.interaction.rest.api.EventsResource;
import org.mabartos.meetmethere.interaction.rest.api.model.EventJson;
import org.mabartos.meetmethere.interaction.rest.api.model.ModelToJson;

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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
    public Uni<Set<EventJson>> getEvents(@QueryParam(FIRST_RESULT) int firstResult, @QueryParam(MAX_RESULTS) int maxResults) {
        return getSetOfEvents(session.eventBus(), EventService.EVENT_GET_EVENTS_EVENT, new PaginationObject(firstResult, maxResults));
    }

    @Path("/{id}")
    public EventResource getEventById(@PathParam(ID) Long id) {
        return new EventResourceProvider(session, id);
    }

    @GET
    @Path("/{title}")
    public Uni<Set<EventJson>> searchEventsByTitle(@PathParam("title") String title) {
        return getSetOfEvents(session.eventBus(), EventService.EVENT_SEARCH_TITLE_EVENT, title);
    }

    @GET
    @Path("/coordinates")
    public Uni<Set<EventJson>> searchEventsByCoordinates(@QueryParam("longitude") Double longitude,
                                                         @QueryParam("latitude") Double latitude) {
        if (longitude == null || latitude == null) {
            throw new BadRequestException("You need to specify both longitude and latitude.");
        }

        return getSetOfEvents(session.eventBus(), EventService.EVENT_SEARCH_COORDINATES_EVENT, new Coordinates(longitude, latitude));
    }

    @POST
    public Uni<EventJson> createEvent(EventJson event) {
        return getSingleEvent(session.eventBus(), EventService.EVENT_CREATE_EVENT, event);
    }

    @GET
    @Path("/count")
    public Uni<Long> getEventsCount() {
        return session.eventBus().<Long>request(EventService.EVENT_COUNT_EVENT, null).onItem().transform(Message::body);
    }

    protected static Uni<EventJson> getSingleEvent(EventBus bus, String address, Object object) {
        return bus.<EventModel>request(address, object).onItem().transform(Message::body).map(ModelToJson::toJson);
    }

    protected static Uni<Set<EventJson>> getSetOfEvents(EventBus bus, String address, Object object) {
        return bus.<SetHolder<EventModel>>request(address, object)
                .onItem()
                .transform(Message::body)
                .map(f -> f.getSet()
                        .stream()
                        .filter(Objects::nonNull)
                        .map(ModelToJson::toJson)
                        .collect(Collectors.toSet())
                );
    }
}
