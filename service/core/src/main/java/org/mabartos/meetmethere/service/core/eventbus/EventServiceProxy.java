package org.mabartos.meetmethere.service.core.eventbus;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Uni;
import org.mabartos.meetmethere.api.domain.Event;
import org.mabartos.meetmethere.api.model.Coordinates;
import org.mabartos.meetmethere.api.model.eventbus.PaginationObject;
import org.mabartos.meetmethere.api.model.eventbus.event.EventSet;
import org.mabartos.meetmethere.api.model.exception.ModelDuplicateException;
import org.mabartos.meetmethere.api.service.EventService;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import static org.mabartos.meetmethere.api.model.eventbus.event.EventEventsNames.EVENT_COUNT_EVENT;
import static org.mabartos.meetmethere.api.model.eventbus.event.EventEventsNames.EVENT_CREATE_EVENT;
import static org.mabartos.meetmethere.api.model.eventbus.event.EventEventsNames.EVENT_GET_EVENTS_EVENT;
import static org.mabartos.meetmethere.api.model.eventbus.event.EventEventsNames.EVENT_GET_EVENT_EVENT;
import static org.mabartos.meetmethere.api.model.eventbus.event.EventEventsNames.EVENT_REMOVE_EVENT;
import static org.mabartos.meetmethere.api.model.eventbus.event.EventEventsNames.EVENT_SEARCH_COORDINATES_EVENT;
import static org.mabartos.meetmethere.api.model.eventbus.event.EventEventsNames.EVENT_SEARCH_TITLE_EVENT;
import static org.mabartos.meetmethere.api.model.eventbus.event.EventEventsNames.EVENT_UPDATE_EVENT;

@ApplicationScoped
@Transactional
public class EventServiceProxy {
    private final MeetMeThereSession session;
    private final EventService delegate;

    @Inject
    public EventServiceProxy(MeetMeThereSession session, EventService delegate) {
        this.session = session;
        this.delegate = delegate;
    }

    @ConsumeEvent(value = EVENT_CREATE_EVENT, blocking = true)
    public Uni<Long> createEvent(Event event) throws ModelDuplicateException {
        return delegate.createEvent(event);
    }

    @ConsumeEvent(value = EVENT_UPDATE_EVENT, blocking = true)
    public Uni<Event> updateEvent(Event event) {
        return delegate.updateEvent(event);
    }

    @ConsumeEvent(value = EVENT_GET_EVENTS_EVENT, blocking = true)
    public Uni<EventSet> getEvents(PaginationObject paginationObject) {
        return delegate.getEvents(paginationObject.getFirstResult(), paginationObject.getMaxResult())
                .onItem()
                .transform(EventSet::new);
    }

    @ConsumeEvent(value = EVENT_SEARCH_TITLE_EVENT, blocking = true)
    public Uni<EventSet> searchEventsByTitle(String title) {
        return delegate.searchEventsByTitle(title)
                .onItem()
                .transform(EventSet::new);
    }

    @ConsumeEvent(value = EVENT_SEARCH_COORDINATES_EVENT, blocking = true)
    public Uni<EventSet> searchEventsByCoordinates(Coordinates coordinates) {
        return delegate.searchEventsByCoordinates(coordinates)
                .onItem()
                .transform(EventSet::new);
    }

    @ConsumeEvent(value = EVENT_GET_EVENT_EVENT, blocking = true)
    public Uni<Event> getEvent(Long id) {
        return delegate.getEvent(id);
    }

    @ConsumeEvent(value = EVENT_COUNT_EVENT, blocking = true)
    public Uni<Long> getEventsCount(Object ignore) {
        return delegate.getEventsCount();
    }

    @ConsumeEvent(value = EVENT_REMOVE_EVENT, blocking = true)
    public void removeEvent(Long eventId) {
        delegate.removeEvent(eventId);
    }
}
