package org.mabartos.meetmethere.model.caffeine.provider;

import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheName;
import io.quarkus.cache.CacheResult;
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
    public EventModel getEventById(Long id) {
        return searchCache(id, v -> secondLevelStore.getEventById(id));
    }

    @CacheInvalidate(cacheName = CACHE_NAME)
    public void invalidateById(Long id) {
    }

    @Override
    public Set<EventModel> searchByTitle(String title) {
        return searchCache(title, v -> secondLevelStore.searchByTitle(title));
    }

    @Override
    public Set<EventModel> searchByCoordinates(Coordinates coordinates) {
        return searchCache(coordinates, v -> secondLevelStore.searchByCoordinates(coordinates));
    }

    @Override
    @CacheResult(cacheName = CACHE_NAME)
    public Set<EventModel> getEvents(int firstResult, int maxResults) {
        return secondLevelStore.getEvents(firstResult, maxResults);
    }

    @Override
    @CacheResult(cacheName = CACHE_NAME)
    public long getEventsCount() {
        return secondLevelStore.getEventsCount();
    }

    @Override
    public EventModel createEvent(EventModel event) throws ModelDuplicateException {
        return secondLevelStore.createEvent(event);
    }

    @Override
    public EventModel createEvent(String title, UserModel creator) {
        return secondLevelStore.createEvent(title, creator);
    }

    @Override
    public void removeEvent(Long id) {
        invalidateById(id);
        secondLevelStore.removeEvent(id);
    }

    @Override
    public EventModel updateEvent(EventModel event) {
        invalidateById(event.getId());
        return secondLevelStore.updateEvent(event);
    }
}
