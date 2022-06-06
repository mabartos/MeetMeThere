package org.mabartos.meetmethere.model.caffeine.provider;

import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheName;
import io.quarkus.cache.CacheResult;
import org.mabartos.meetmethere.api.model.AddressModel;
import org.mabartos.meetmethere.api.model.Coordinates;
import org.mabartos.meetmethere.api.model.EventModel;
import org.mabartos.meetmethere.api.model.UserModel;
import org.mabartos.meetmethere.api.model.exception.ModelDuplicateException;
import org.mabartos.meetmethere.api.provider.EventProvider;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Set;
import java.util.function.Function;

@ApplicationScoped
public class CaffeineEventProvider implements EventProvider {
    private static final String CACHE_NAME = "event-cache";

    private final MeetMeThereSession session;
    private final EventProvider secondLevelStore;

    @Inject
    @CacheName(CACHE_NAME)
    Cache cache;

    public CaffeineEventProvider(MeetMeThereSession session, EventProvider secondLevelStore) {
        this.session = session;
        this.secondLevelStore = secondLevelStore;
    }

    private <T, U> T searchCache(U key, Function<U, T> function) {
        return CacheUtil.searchCache(cache, key, function);
    }

    @Override
    @CacheResult(cacheName = CACHE_NAME)
    public EventModel getEventById(Long id) {
        return secondLevelStore.getEventById(id);
    }

    @CacheInvalidate(cacheName = CACHE_NAME)
    public void invalidateById(Long id) {
    }

    @CacheInvalidate(cacheName = CACHE_NAME)
    public void invalidateByTitle(String title) {
    }

    @CacheInvalidate(cacheName = CACHE_NAME)
    public void invalidateByCoordinates(Coordinates coordinates) {
    }

    @CacheInvalidateAll(cacheName = CACHE_NAME)
    public void invalidateAll() {
    }

    @Override
    @CacheResult(cacheName = CACHE_NAME)
    public Set<EventModel> searchByTitle(String title) {
        return secondLevelStore.searchByTitle(title);
    }

    @Override
    @CacheResult(cacheName = CACHE_NAME)
    public Set<EventModel> searchByCoordinates(Coordinates coordinates) {
        return secondLevelStore.searchByCoordinates(coordinates);
    }

    @Override
    @CacheResult(cacheName = CACHE_NAME)
    public Set<EventModel> getEvents(int firstResult, int maxResults) {
        return secondLevelStore.getEvents(firstResult, maxResults);
    }

    @Override
    @CacheResult(cacheName = CACHE_NAME)
    public Set<EventModel> getEventsByUser(String userId) {
        return secondLevelStore.getEventsByUser(userId);
    }

    @Override
    @CacheResult(cacheName = CACHE_NAME)
    public Set<EventModel> getEventsByOrganizator(String userId) {
        return secondLevelStore.getEventsByOrganizator(userId);
    }

    @Override
    @CacheResult(cacheName = CACHE_NAME)
    public long getEventsCount() {
        return secondLevelStore.getEventsCount();
    }

    @Override
    public EventModel createEvent(EventModel event) throws ModelDuplicateException {
        final EventModel result = secondLevelStore.createEvent(event);
        invalidateById(result.getId());
        invalidateByTitle(event.getEventTitle());
        invalidateAll();
        return result;
    }

    @Override
    public EventModel createEvent(String title, UserModel creator) {
        final EventModel result = secondLevelStore.createEvent(title, creator);
        invalidateById(result.getId());
        invalidateByTitle(title);
        invalidateAll();
        return result;
    }

    @Override
    public EventModel createEvent(String title, String creatorName) {
        final EventModel result = secondLevelStore.createEvent(title, creatorName);
        invalidateById(result.getId());
        invalidateByTitle(title);
        invalidateAll();
        return result;
    }

    @Override
    public void removeEvent(Long id) {
        invalidateById(id);
        secondLevelStore.removeEvent(id);
    }

    @Override
    public EventModel updateEvent(EventModel event) {
        invalidateById(event.getId());
        invalidateByTitle(event.getEventTitle());
        final AddressModel venue = event.getVenue();
        if (venue != null && venue.getCoordinates() != null) {
            invalidateByCoordinates(venue.getCoordinates());
        }

        return secondLevelStore.updateEvent(event);
    }
}
