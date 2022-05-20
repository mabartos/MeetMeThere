package org.mabartos.meetmethere.service.core.factory;

import org.mabartos.meetmethere.api.provider.ProviderFactory;
import org.mabartos.meetmethere.api.service.UserService;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;
import org.mabartos.meetmethere.service.core.DefaultUserService;

public class UserServiceFactory implements ProviderFactory<UserService> {

    @Override
    public UserService create(MeetMeThereSession session) {
        return new DefaultUserService(session);
    }
}
