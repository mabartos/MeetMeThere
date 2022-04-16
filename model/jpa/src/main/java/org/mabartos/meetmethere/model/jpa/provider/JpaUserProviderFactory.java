package org.mabartos.meetmethere.model.jpa.provider;

import org.mabartos.meetmethere.model.provider.ProviderFactory;
import org.mabartos.meetmethere.model.provider.UserProvider;
import org.mabartos.meetmethere.session.MeetMeThereSession;

public class JpaUserProviderFactory implements ProviderFactory<UserProvider> {

    @Override
    public UserProvider create(MeetMeThereSession session) {
        return new JpaUserProvider(session, session.entityManager());
    }
}
