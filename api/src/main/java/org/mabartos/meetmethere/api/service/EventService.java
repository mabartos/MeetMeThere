package org.mabartos.meetmethere.api.service;

import io.smallrye.mutiny.Uni;
import org.mabartos.meetmethere.api.domain.Event;
import org.mabartos.meetmethere.api.model.Coordinates;
import org.mabartos.meetmethere.api.model.exception.ModelDuplicateException;

import java.util.Set;

public interface EventService {
    Uni<Long> createEvent(Event event) throws ModelDuplicateException;

    Uni<Event> updateEvent(Event event);

    Uni<Set<Event>> getEvents(Integer firstResult, Integer maxResult);

    Uni<Set<Event>> searchEventsByTitle(String title);

    Uni<Set<Event>> searchEventsByCoordinates(Coordinates coordinates);

    Uni<Event> getEvent(Long id);

    Uni<Long> getEventsCount();

    void removeEvent(Long eventId);
}
