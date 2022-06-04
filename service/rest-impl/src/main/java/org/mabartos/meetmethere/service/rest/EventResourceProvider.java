package org.mabartos.meetmethere.service.rest;

import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheResult;
import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
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

import static org.mabartos.meetmethere.api.model.eventbus.event.EventEventsNames.EVENT_GET_EVENT_EVENT;
import static org.mabartos.meetmethere.api.model.eventbus.event.EventEventsNames.EVENT_REMOVE_EVENT;
import static org.mabartos.meetmethere.api.model.eventbus.event.EventEventsNames.EVENT_UPDATE_EVENT;
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
    @CacheResult(cacheName = EventsResourceProvider.CACHE_NAME)
    public Uni<EventJson> getEvent() {
        return getSingleEvent(session.eventBus(), EVENT_GET_EVENT_EVENT, eventId);
    }

    @DELETE
    @Authenticated
    public Response removeEvent() {
        invalidateById(eventId);
        session.eventBus().publish(EVENT_REMOVE_EVENT, eventId);
        return Response.ok().build();
    }

    @PATCH
    @Authenticated
    public Uni<EventJson> updateEvent(EventJson event) {
        if (event.getId() != null && !eventId.equals(event.getId())) {
            throw new BadRequestException("Cannot update Event - different IDs");
        }
        event.setId(eventId);

        invalidateById(eventId);
        invalidateByTitle(event.getTitle());

        return getSingleEvent(session.eventBus(), EVENT_UPDATE_EVENT, mapper.toDomain(event));
    }

    @Path("/invitations")
    public EventInvitationsResource getInvitations() {
        return new EventInvitationsResourceProvider(session, eventId);
    }

    @CacheInvalidate(cacheName = EventsResourceProvider.CACHE_NAME)
    void invalidateById(Long id) {

    }

    @CacheInvalidate(cacheName = EventsResourceProvider.CACHE_NAME)
    void invalidateByTitle(String title) {

    }
}
