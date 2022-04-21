package org.mabartos.meetmethere.service.core;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Uni;
import org.mabartos.meetmethere.api.domain.Event;
import org.mabartos.meetmethere.api.model.Coordinates;
import org.mabartos.meetmethere.api.model.EventModel;
import org.mabartos.meetmethere.api.model.ModelUpdater;
import org.mabartos.meetmethere.api.model.UserModel;
import org.mabartos.meetmethere.api.model.eventbus.EventModelSet;
import org.mabartos.meetmethere.api.model.eventbus.PaginationObject;
import org.mabartos.meetmethere.api.model.exception.ModelDuplicateException;
import org.mabartos.meetmethere.api.model.exception.ModelNotFoundException;
import org.mabartos.meetmethere.api.service.EventService;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public class DefaultEventService implements EventService {

    MeetMeThereSession session;

    @Inject
    public DefaultEventService(MeetMeThereSession session) {
        this.session = session;
    }

    @Override
    @ConsumeEvent(value = EVENT_CREATE_EVENT, blocking = true)
    public Uni<EventModel> createEvent(Event event) throws ModelDuplicateException {
        if (event.getId() != null && session.eventsStorage().getEventById(event.getId()) != null) {
            throw new ModelDuplicateException("Event already exists with the id.");
        }

        final UserModel creator = session.users().getUserById(event.getCreatedById());

        EventModel model = session.eventsStorage().createEvent(event.getTitle(), creator);
        ModelUpdater.updateModel(event, model);

        return Uni.createFrom().item(session.eventsStorage().updateEvent(model));

    }

    @Override
    @ConsumeEvent(value = EVENT_UPDATE_EVENT, blocking = true)
    public Uni<EventModel> updateEvent(Event event) throws ModelNotFoundException {
        EventModel model = Optional.ofNullable(session.eventsStorage().getEventById(event.getId()))
                .orElseThrow(ModelNotFoundException::new);

        try {
            ModelUpdater.updateModel(event, model);
            return Uni.createFrom().item(session.eventsStorage().updateEvent(model));
        } catch (IllegalArgumentException e) {
            throw new ModelNotFoundException();
        }
    }

    @Override
    @ConsumeEvent(value = EVENT_GET_EVENTS_EVENT, blocking = true)
    public Uni<EventModelSet> getEvents(PaginationObject paginationObject) {
        return Uni.createFrom()
                .item(new EventModelSet(session.eventsStorage()
                        .getEvents(paginationObject.getFirstResult(), paginationObject.getMaxResult())));
    }

    @Override
    @ConsumeEvent(value = EVENT_SEARCH_TITLE_EVENT, blocking = true)
    public Uni<EventModelSet> searchEventsByTitle(String title) {
        final Set<EventModel> events = session.eventsStorage().searchByTitle(title);
        return Uni.createFrom().item(new EventModelSet(events));
    }

    @Override
    @ConsumeEvent(value = EVENT_SEARCH_COORDINATES_EVENT, blocking = true)
    public Uni<EventModelSet> searchEventsByCoordinates(Coordinates coordinates) {
        final Set<EventModel> events = session.eventsStorage().searchByCoordinates(coordinates);
        return Uni.createFrom().item(new EventModelSet(events));
    }

    @Override
    @ConsumeEvent(value = EVENT_GET_EVENT_EVENT, blocking = true)
    public Uni<EventModel> getEvent(Long id) {
        return Uni.createFrom().item(session.eventsStorage().getEventById(id));
    }

    @Override
    @ConsumeEvent(value = EVENT_COUNT_EVENT, blocking = true)
    public Uni<Long> getEventsCount(Object ignore) {
        return Uni.createFrom().item(session.eventsStorage().getEventsCount());
    }

    @Override
    @ConsumeEvent(value = EVENT_REMOVE_EVENT, blocking = true)
    public void removeEvent(Long eventId) throws ModelNotFoundException {
        session.eventsStorage().removeEvent(eventId);
    }
}
