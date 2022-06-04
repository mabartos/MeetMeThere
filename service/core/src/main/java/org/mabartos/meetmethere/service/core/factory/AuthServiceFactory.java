package org.mabartos.meetmethere.service.core.factory;

import org.mabartos.meetmethere.api.provider.ProviderFactory;
import org.mabartos.meetmethere.api.service.AuthService;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;
import org.mabartos.meetmethere.service.core.DefaultAuthService;

public class AuthServiceFactory implements ProviderFactory<AuthService> {

    @Override
    public AuthService create(MeetMeThereSession session) {
        return new DefaultAuthService(session);
    }
}
