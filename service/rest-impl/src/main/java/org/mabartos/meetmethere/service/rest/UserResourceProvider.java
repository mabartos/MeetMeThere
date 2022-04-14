package org.mabartos.meetmethere.service.rest;

import org.mabartos.meetmethere.interaction.rest.api.UserResource;
import org.mabartos.meetmethere.interaction.rest.api.dto.User;
import org.mabartos.meetmethere.model.UserModel;
import org.mabartos.meetmethere.session.MeetMeThereSession;

public class UserResourceProvider implements UserResource {
    private final MeetMeThereSession session;
    private final UserModel user;
    
    public UserResourceProvider(MeetMeThereSession session, UserModel user) {
        this.session = session;
        this.user = user;
    }

    @Override
    public User getUser() {
        return null;
    }

    @Override
    public void removeUser() {

    }

    @Override
    public User updateUser(User user) {
        return null;
    }
}
