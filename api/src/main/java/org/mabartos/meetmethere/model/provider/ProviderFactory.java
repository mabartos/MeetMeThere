package org.mabartos.meetmethere.model.provider;

import org.mabartos.meetmethere.session.MeetMeThereSession;

public interface ProviderFactory<T> {

    T create(MeetMeThereSession session);
}
