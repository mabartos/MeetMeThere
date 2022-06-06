package org.mabartos.meetmethere.service.rest;

import io.quarkus.cache.CacheResult;
import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.mutiny.core.eventbus.Message;
import org.mabartos.meetmethere.api.domain.Event;
import org.mabartos.meetmethere.api.model.Coordinates;
import org.mabartos.meetmethere.api.model.eventbus.PaginationObject;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;
import org.mabartos.meetmethere.interaction.rest.api.EventResource;
import org.mabartos.meetmethere.interaction.rest.api.EventsResource;
import org.mabartos.meetmethere.interaction.rest.api.model.EventJson;
import org.mabartos.meetmethere.interaction.rest.api.model.mapper.EventJsonDomainMapper;
import org.mabartos.meetmethere.service.rest.util.EventBusUtil;
import org.mapstruct.factory.Mappers;

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

import static org.mabartos.meetmethere.api.model.eventbus.event.EventEventsNames.EVENT_COUNT_EVENT;
import static org.mabartos.meetmethere.api.model.eventbus.event.EventEventsNames.EVENT_CREATE_EVENT;
import static org.mabartos.meetmethere.api.model.eventbus.event.EventEventsNames.EVENT_GET_EVENTS_EVENT;
import static org.mabartos.meetmethere.api.model.eventbus.event.EventEventsNames.EVENT_SEARCH_COORDINATES_EVENT;
import static org.mabartos.meetmethere.api.model.eventbus.event.EventEventsNames.EVENT_SEARCH_TITLE_EVENT;
import static org.mabartos.meetmethere.interaction.rest.api.ResourceConstants.FIRST_RESULT;
import static org.mabartos.meetmethere.interaction.rest.api.ResourceConstants.ID;
import static org.mabartos.meetmethere.interaction.rest.api.ResourceConstants.MAX_RESULTS;

@Path("/events")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
@Transactional
public class EventsResourceProvider implements EventsResource {
    private static final EventJsonDomainMapper mapper = Mappers.getMapper(EventJsonDomainMapper.class);
    static final String CACHE_NAME = "event-resource-provider-cache";

    @Context
    MeetMeThereSession session;

    @GET
    @CacheResult(cacheName = CACHE_NAME)
    public Uni<Set<EventJson>> getEvents(@QueryParam(FIRST_RESULT) Integer firstResult, @QueryParam(MAX_RESULTS) Integer maxResults) {
        session.auth().events().requireView();

        firstResult = firstResult != null ? firstResult : 0;
        maxResults = maxResults != null ? maxResults : Integer.MAX_VALUE;

        return getSetOfEvents(session.eventBus(), EVENT_GET_EVENTS_EVENT, new PaginationObject(firstResult, maxResults));
    }

    @Path("/{id}")
    @CacheResult(cacheName = CACHE_NAME)
    public EventResource getEventById(@PathParam(ID) Long id) {
        session.auth().events().requireViewId(id);

        return new EventResourceProvider(session, id);
    }

    @GET
    @Path("/title/{title}")
    @CacheResult(cacheName = CACHE_NAME)
    public Uni<Set<EventJson>> searchEventsByTitle(@PathParam("title") String title) {
        return getSetOfEvents(session.eventBus(), EVENT_SEARCH_TITLE_EVENT, title);
    }

    @GET
    @Path("/coordinates")
    @CacheResult(cacheName = CACHE_NAME)
    public Uni<Set<EventJson>> searchEventsByCoordinates(@QueryParam("longitude") Double longitude,
                                                         @QueryParam("latitude") Double latitude) {
        if (longitude == null || latitude == null) {
            throw new BadRequestException("You need to specify both longitude and latitude.");
        }

        return getSetOfEvents(session.eventBus(), EVENT_SEARCH_COORDINATES_EVENT, new Coordinates(longitude, latitude));
    }

    @POST
    public Uni<Long> createEvent(EventJson event) {
        return EventBusUtil.createEntity(session.eventBus(), EVENT_CREATE_EVENT, mapper.toDomain(event));
    }

    @GET
    @Path("/count")
    @CacheResult(cacheName = CACHE_NAME)
    public Uni<Long> getEventsCount() {
        return session.eventBus().<Long>request(EVENT_COUNT_EVENT, null).onItem().transform(Message::body);
    }

    protected static Uni<EventJson> getSingleEvent(EventBus bus, String address, Object object) {
        return EventBusUtil.<Event>getSingleEntity(bus, address, object).map(mapper::toJson);
    }

    protected static Uni<Set<EventJson>> getSetOfEvents(EventBus bus, String address, Object object) {
        return EventBusUtil.getSetOfEntities(bus, address, object, mapper::toJson);
    }
}
