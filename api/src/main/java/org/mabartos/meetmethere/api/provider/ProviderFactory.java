package org.mabartos.meetmethere.api.provider;

import org.mabartos.meetmethere.api.session.MeetMeThereSession;

public interface ProviderFactory<T> {

    T create(MeetMeThereSession session);
}
