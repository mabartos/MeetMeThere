package org.mabartos.meetmethere.api.service;

import io.smallrye.mutiny.Uni;
import org.mabartos.meetmethere.api.domain.Event;
import org.mabartos.meetmethere.api.model.Coordinates;
import org.mabartos.meetmethere.api.model.EventModel;
import org.mabartos.meetmethere.api.model.eventbus.EventModelSet;
import org.mabartos.meetmethere.api.model.eventbus.PaginationObject;
import org.mabartos.meetmethere.api.model.exception.ModelDuplicateException;
import org.mabartos.meetmethere.api.model.exception.ModelNotFoundException;

public interface EventService {
    String EVENT_CREATE_EVENT = "eventCreateEvent";
    String EVENT_UPDATE_EVENT = "eventUpdateEvent";
    String EVENT_REMOVE_EVENT = "eventRemoveEvent";
    String EVENT_GET_EVENTS_EVENT = "eventGetMultipleEvent";
    String EVENT_GET_EVENT_EVENT = "eventGetSingleEvent";
    String EVENT_SEARCH_TITLE_EVENT = "eventSearchByTitleEvent";
    String EVENT_SEARCH_COORDINATES_EVENT = "eventSearchByCoordinatesEvent";
    String EVENT_COUNT_EVENT = "eventCountEvent";

    Uni<EventModel> createEvent(Event event) throws ModelDuplicateException;

    Uni<EventModel> updateEvent(Event event) throws ModelNotFoundException;

    Uni<EventModelSet> getEvents(PaginationObject paginationObject);

    Uni<EventModelSet> searchEventsByTitle(String title);

    Uni<EventModelSet> searchEventsByCoordinates(Coordinates coordinates);

    Uni<EventModel> getEvent(Long id);

    Uni<Long> getEventsCount(Object ignore);

    void removeEvent(Long eventId) throws ModelNotFoundException;
}
