package org.mabartos.meetmethere.api.provider;

import org.mabartos.meetmethere.api.session.MeetMeThereSession;

public interface StoreProviderFactory<T> {

    T createParentStore(MeetMeThereSession session, T secondLevelStore);

    T createChildStore(MeetMeThereSession session);
}
