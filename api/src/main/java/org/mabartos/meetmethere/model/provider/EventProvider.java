package org.mabartos.meetmethere.model.provider;

import org.mabartos.meetmethere.model.Coordinates;
import org.mabartos.meetmethere.model.EventModel;

import java.util.Set;

public interface EventProvider {

    EventModel getEventById(Long id);

    Set<EventModel> searchByTitle(String title);

    Set<EventModel> searchByCoordinates(Coordinates coordinates);

    Set<EventModel> getEvents(int firstResult, int maxResults);

    int getEventsCount();

    void createEvent(EventModel event);

    void removeEvent(Long id);

    void updateEvent(Long id, EventModel event);
}
