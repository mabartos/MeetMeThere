package org.mabartos.meetmethere.api.model.jpa.provider;

import org.mabartos.meetmethere.api.provider.ProviderFactory;
import org.mabartos.meetmethere.api.provider.UserProvider;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;

public class JpaUserProviderFactory implements ProviderFactory<UserProvider> {

    @Override
    public UserProvider create(MeetMeThereSession session) {
        return new JpaUserProvider(session, session.entityManager());
    }
}
