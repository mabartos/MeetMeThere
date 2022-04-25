package org.mabartos.meetmethere.service.core;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Uni;
import org.mabartos.meetmethere.api.domain.Event;
import org.mabartos.meetmethere.api.mapper.ModelToDomain;
import org.mabartos.meetmethere.api.model.Coordinates;
import org.mabartos.meetmethere.api.model.EventModel;
import org.mabartos.meetmethere.api.model.ModelUpdater;
import org.mabartos.meetmethere.api.model.UserModel;
import org.mabartos.meetmethere.api.model.eventbus.EventSet;
import org.mabartos.meetmethere.api.model.eventbus.PaginationObject;
import org.mabartos.meetmethere.api.model.exception.ModelDuplicateException;
import org.mabartos.meetmethere.api.model.exception.ModelNotFoundException;
import org.mabartos.meetmethere.api.service.EventService;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class DefaultEventService implements EventService {

    MeetMeThereSession session;

    @Inject
    public DefaultEventService(MeetMeThereSession session) {
        this.session = session;
    }

    @Override
    @ConsumeEvent(value = EVENT_CREATE_EVENT, blocking = true)
    public Uni<Long> createEvent(Event event) throws ModelDuplicateException {
        if (event.getId() != null && session.eventStorage().getEventById(event.getId()) != null) {
            throw new ModelDuplicateException("Event already exists with the id.");
        }

        final UserModel creator = session.userStorage().getUserById(event.getCreatedById());

        EventModel model = session.eventStorage().createEvent(event.getTitle(), creator);
        ModelUpdater.updateModel(event, model);

        return Uni.createFrom().item(session.eventStorage().updateEvent(model).getId());

    }

    @Override
    @ConsumeEvent(value = EVENT_UPDATE_EVENT, blocking = true)
    public Uni<Event> updateEvent(Event event) throws ModelNotFoundException {
        EventModel model = Optional.ofNullable(session.eventStorage().getEventById(event.getId()))
                .orElseThrow(ModelNotFoundException::new);

        try {
            ModelUpdater.updateModel(event, model);
            return Uni.createFrom().item(session.eventStorage().updateEvent(model))
                    .onItem()
                    .transform(ModelToDomain::toDomain);
        } catch (IllegalArgumentException e) {
            throw new ModelNotFoundException();
        }
    }

    @Override
    @ConsumeEvent(value = EVENT_GET_EVENTS_EVENT, blocking = true)
    public Uni<EventSet> getEvents(PaginationObject paginationObject) {
        Set<Event> eventSet = session.eventStorage()
                .getEvents(paginationObject.getFirstResult(), paginationObject.getMaxResult())
                .stream()
                .map(ModelToDomain::toDomain)
                .collect(Collectors.toSet());

        return Uni.createFrom().item(new EventSet(eventSet));
    }

    @Override
    @ConsumeEvent(value = EVENT_SEARCH_TITLE_EVENT, blocking = true)
    public Uni<EventSet> searchEventsByTitle(String title) {
        final Set<Event> events = session.eventStorage()
                .searchByTitle(title)
                .stream()
                .map(ModelToDomain::toDomain)
                .collect(Collectors.toSet());
        return Uni.createFrom().item(new EventSet(events));
    }

    @Override
    @ConsumeEvent(value = EVENT_SEARCH_COORDINATES_EVENT, blocking = true)
    public Uni<EventSet> searchEventsByCoordinates(Coordinates coordinates) {
        final Set<Event> events = session.eventStorage()
                .searchByCoordinates(coordinates)
                .stream()
                .map(ModelToDomain::toDomain)
                .collect(Collectors.toSet());
        return Uni.createFrom().item(new EventSet(events));
    }

    @Override
    @ConsumeEvent(value = EVENT_GET_EVENT_EVENT, blocking = true)
    public Uni<Event> getEvent(Long id) {
        return Uni.createFrom()
                .item(session.eventStorage().getEventById(id))
                .onItem()
                .transform(ModelToDomain::toDomain);
    }

    @Override
    @ConsumeEvent(value = EVENT_COUNT_EVENT, blocking = true)
    public Uni<Long> getEventsCount(Object ignore) {
        return Uni.createFrom().item(session.eventStorage().getEventsCount());
    }

    @Override
    @ConsumeEvent(value = EVENT_REMOVE_EVENT, blocking = true)
    public void removeEvent(Long eventId) throws ModelNotFoundException {
        session.eventStorage().removeEvent(eventId);
    }
}
