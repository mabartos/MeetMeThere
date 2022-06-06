package org.mabartos.meetmethere.api.provider;

import org.mabartos.meetmethere.api.model.AddressModel;
import org.mabartos.meetmethere.api.model.Coordinates;
import org.mabartos.meetmethere.api.model.EventModel;
import org.mabartos.meetmethere.api.model.UserModel;
import org.mabartos.meetmethere.api.model.exception.ModelDuplicateException;

import java.util.Set;

public interface EventProvider {

    EventModel getEventById(Long id);

    Set<EventModel> searchByTitle(String title);

    Set<EventModel> searchByCoordinates(Coordinates coordinates);

    Set<EventModel> getEvents(int firstResult, int maxResults);

    Set<EventModel> getEventsByUser(String userId);

    Set<EventModel> getEventsByOrganizator(String userId);

    long getEventsCount();

    EventModel createEvent(EventModel event) throws ModelDuplicateException;

    EventModel createEvent(String title, UserModel creator);

    EventModel createEvent(String title, String creatorName);

    void removeEvent(Long id);

    EventModel updateEvent(EventModel event);
}
