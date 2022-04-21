package org.mabartos.meetmethere.api.provider;

import org.mabartos.meetmethere.api.model.Coordinates;
import org.mabartos.meetmethere.api.model.EventModel;
import org.mabartos.meetmethere.api.model.UserModel;
import org.mabartos.meetmethere.api.model.exception.ModelDuplicateException;
import org.mabartos.meetmethere.api.model.exception.ModelNotFoundException;

import java.util.Set;

public interface EventProvider {

    EventModel getEventById(Long id);

    Set<EventModel> searchByTitle(String title);

    Set<EventModel> searchByCoordinates(Coordinates coordinates);

    Set<EventModel> getEvents(int firstResult, int maxResults);

    long getEventsCount();

    EventModel createEvent(EventModel event) throws ModelDuplicateException;

    EventModel createEvent(String title, UserModel creator);

    void removeEvent(Long id) throws ModelNotFoundException;

    EventModel updateEvent(EventModel event);
}
