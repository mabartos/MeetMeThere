package org.mabartos.meetmethere.model.jpa.provider;

import org.mabartos.meetmethere.api.provider.ProviderFactory;
import org.mabartos.meetmethere.api.provider.UserProvider;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;

@Deprecated
public class JpaUserProviderFactory implements ProviderFactory<UserProvider> {

    @Override
    public UserProvider create(MeetMeThereSession session) {
        return new JpaUserProvider(session, session.entityManager());
    }
}
