package org.mabartos.meetmethere.model.caffeine.provider;

import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheName;
import io.quarkus.cache.CacheResult;
import org.mabartos.meetmethere.api.model.UserModel;
import org.mabartos.meetmethere.api.model.exception.ModelDuplicateException;
import org.mabartos.meetmethere.api.provider.UserProvider;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;

import javax.inject.Inject;
import java.util.Set;
import java.util.function.Function;

public class CaffeineUserProvider implements UserProvider {
    private static final String CACHE_NAME = "user-cache";

    private final MeetMeThereSession session;
    private final UserProvider secondLevelStore;

    @Inject
    @CacheName(CACHE_NAME)
    Cache cache;

    public CaffeineUserProvider(MeetMeThereSession session, UserProvider secondLevelStore) {
        this.session = session;
        this.secondLevelStore = secondLevelStore;
    }

    private <T, U> T searchCache(U key, Function<U, T> function) {
        return CacheUtil.searchCache(cache, key, function);
    }

    @CacheInvalidate(cacheName = CACHE_NAME)
    public void invalidateById(Long id) {
    }

    @Override
    public UserModel getUserById(Long id) {
        return searchCache(id, v -> secondLevelStore.getUserById(id));
    }

    @Override
    public UserModel getUserByUsername(String username) {
        return searchCache(username, v -> secondLevelStore.getUserByUsername(username));
    }

    @Override
    public UserModel getUserByEmail(String email) {
        return searchCache(email, v -> secondLevelStore.getUserByEmail(email));
    }

    @Override
    @CacheResult(cacheName = CACHE_NAME)
    public Set<UserModel> getUsers(int firstResult, int maxResults) {
        return secondLevelStore.getUsers(firstResult, maxResults);
    }

    @Override
    @CacheResult(cacheName = CACHE_NAME)
    public long getUsersCount() {
        return secondLevelStore.getUsersCount();
    }

    @Override
    public UserModel createUser(String email, String username) throws ModelDuplicateException {
        return secondLevelStore.createUser(email, username);
    }

    @Override
    public UserModel createUser(UserModel user) throws ModelDuplicateException {
        return secondLevelStore.createUser(user);
    }

    @Override
    public void removeUser(Long id) {
        invalidateById(id);
        secondLevelStore.removeUser(id);
    }

    @Override
    public UserModel updateUser(UserModel user) {
        invalidateById(user.getId());
        return secondLevelStore.updateUser(user);
    }
}
