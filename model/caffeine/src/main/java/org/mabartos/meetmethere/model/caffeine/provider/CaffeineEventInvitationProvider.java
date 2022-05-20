package org.mabartos.meetmethere.model.caffeine.provider;

import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheName;
import org.mabartos.meetmethere.api.model.EventModel;
import org.mabartos.meetmethere.api.model.InvitationModel;
import org.mabartos.meetmethere.api.model.UserModel;
import org.mabartos.meetmethere.api.model.exception.ModelDuplicateException;
import org.mabartos.meetmethere.api.model.exception.ModelNotFoundException;
import org.mabartos.meetmethere.api.provider.InvitationProvider;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;

import javax.inject.Inject;
import java.util.Set;
import java.util.function.Function;

public class CaffeineEventInvitationProvider implements InvitationProvider {
    private static final String CACHE_NAME = "event-invitation-cache";

    private final MeetMeThereSession session;
    private final InvitationProvider secondLevelStore;

    @Inject
    @CacheName(CACHE_NAME)
    Cache cache;

    public CaffeineEventInvitationProvider(MeetMeThereSession session, InvitationProvider secondLevelStore) {
        this.session = session;
        this.secondLevelStore = secondLevelStore;
    }

    private <T, U> T searchCache(U key, Function<U, T> function) {
        return CacheUtil.searchCache(cache, key, function);
    }

    @CacheInvalidate(cacheName = CACHE_NAME)
    public void invalidateById(Long id) {
    }

    @CacheInvalidate(cacheName = CACHE_NAME)
    public void invalidateByEventId(Long eventId) {
    }

    @Override
    public InvitationModel getInvitationById(Long id) {
        return searchCache(id, v -> secondLevelStore.getInvitationById(id));
    }

    @Override
    public Set<InvitationModel> getInvitationsForEvent(Long eventId) {
        return searchCache(eventId, v -> secondLevelStore.getInvitationsForEvent(eventId));
    }

    @Override
    public InvitationModel createInvitation(InvitationModel invitation) throws ModelDuplicateException {
        return secondLevelStore.createInvitation(invitation);
    }

    @Override
    public InvitationModel createInvitation(EventModel event, UserModel sender, UserModel receiver) {
        return secondLevelStore.createInvitation(event, sender, receiver);
    }

    @Override
    public InvitationModel createInvitation(EventModel event, UserModel sender, UserModel receiver, String message) {
        return secondLevelStore.createInvitation(event, sender, receiver, message);
    }

    @Override
    public void createInvitations(EventModel event, UserModel sender, Set<UserModel> receivers, String message) {
        secondLevelStore.createInvitations(event, sender, receivers, message);
    }

    @Override
    public void removeInvitation(Long id) throws ModelNotFoundException {
        secondLevelStore.removeInvitation(id);
    }

    @Override
    public InvitationModel updateInvitation(InvitationModel invitation) {
        invalidateById(invitation.getId());
        final EventModel event = invitation.getEvent();
        if (event != null) {
            invalidateByEventId(event.getId());
        }
        return secondLevelStore.updateInvitation(invitation);
    }
}
