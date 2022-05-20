package org.mabartos.meetmethere.service.core.store;

import org.mabartos.meetmethere.api.provider.ProviderFactory;
import org.mabartos.meetmethere.api.provider.UserProvider;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;
import org.mabartos.meetmethere.model.caffeine.provider.CaffeineUserProvider;
import org.mabartos.meetmethere.model.jpa.provider.JpaUserProviderFactory;

public class UserStoreFactory implements ProviderFactory<UserProvider> {

    @Override
    public UserProvider create(MeetMeThereSession session) {
        return new CaffeineUserProvider(session, new JpaUserProviderFactory().create(session));
    }
}
