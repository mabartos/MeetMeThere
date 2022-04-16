package org.mabartos.meetmethere.model.provider;

import org.mabartos.meetmethere.model.Coordinates;
import org.mabartos.meetmethere.model.EventModel;
import org.mabartos.meetmethere.model.exception.ModelDuplicateException;
import org.mabartos.meetmethere.model.exception.ModelNotFoundException;

import java.util.Set;

public interface EventProvider {

    EventModel getEventById(Long id);

    Set<EventModel> searchByTitle(String title);

    Set<EventModel> searchByCoordinates(Coordinates coordinates);

    Set<EventModel> getEvents(int firstResult, int maxResults);

    long getEventsCount();

    EventModel createEvent(EventModel event) throws ModelDuplicateException;

    EventModel createEvent(String title);

    void removeEvent(Long id) throws ModelNotFoundException;

    EventModel updateEvent(EventModel event);
}
