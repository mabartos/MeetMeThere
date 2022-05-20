package org.mabartos.meetmethere.service.core;

import io.smallrye.mutiny.Uni;
import org.mabartos.meetmethere.api.domain.Event;
import org.mabartos.meetmethere.api.mapper.ModelToDomain;
import org.mabartos.meetmethere.api.model.Coordinates;
import org.mabartos.meetmethere.api.model.EventModel;
import org.mabartos.meetmethere.api.model.ModelUpdater;
import org.mabartos.meetmethere.api.model.UserModel;
import org.mabartos.meetmethere.api.model.exception.ModelDuplicateException;
import org.mabartos.meetmethere.api.model.exception.ModelNotFoundException;
import org.mabartos.meetmethere.api.service.EventService;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
public class DefaultEventService implements EventService {

    private final MeetMeThereSession session;

    @Inject
    public DefaultEventService(MeetMeThereSession session) {
        this.session = session;
    }

    @Override
    public Uni<Long> createEvent(Event event) throws ModelDuplicateException {
        if (event.getId() != null && session.eventStorage().getEventById(event.getId()) != null) {
            throw new ModelDuplicateException("Event already exists with the id.");
        }

        final UserModel creator = session.userStorage().getUserById(event.getCreatedById());
        if (creator == null) throw new ModelNotFoundException("Cannot find creator");

        EventModel model = session.eventStorage().createEvent(event.getTitle(), creator);
        ModelUpdater.updateModel(event, model);

        return Uni.createFrom().item(session.eventStorage().updateEvent(model).getId());

    }

    @Override
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
    public Uni<Set<Event>> getEvents(Integer firstResult, Integer maxResults) {
        final Set<Event> eventSet = session.eventStorage()
                .getEvents(firstResult, maxResults)
                .stream()
                .map(ModelToDomain::toDomain)
                .collect(Collectors.toSet());

        return Uni.createFrom().item(eventSet);
    }

    @Override
    public Uni<Set<Event>> searchEventsByTitle(String title) {
        final Set<Event> events = session.eventStorage()
                .searchByTitle(title)
                .stream()
                .map(ModelToDomain::toDomain)
                .collect(Collectors.toSet());
        return Uni.createFrom().item(events);
    }

    @Override
    public Uni<Set<Event>> searchEventsByCoordinates(Coordinates coordinates) {
        final Set<Event> events = session.eventStorage()
                .searchByCoordinates(coordinates)
                .stream()
                .map(ModelToDomain::toDomain)
                .collect(Collectors.toSet());
        return Uni.createFrom().item(events);
    }

    @Override
    public Uni<Event> getEvent(Long id) {
        EventModel model = session.eventStorage().getEventById(id);
        if (model == null) throw new ModelNotFoundException();

        return Uni.createFrom()
                .item(model)
                .onItem()
                .transform(ModelToDomain::toDomain);
    }

    @Override
    public Uni<Long> getEventsCount() {
        return Uni.createFrom().item(session.eventStorage().getEventsCount());
    }

    @Override
    public void removeEvent(Long eventId) throws ModelNotFoundException {
        session.eventStorage().removeEvent(eventId);
    }
}
