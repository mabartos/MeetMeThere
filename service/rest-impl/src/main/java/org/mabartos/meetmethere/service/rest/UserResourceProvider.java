package org.mabartos.meetmethere.service.rest;

import io.smallrye.mutiny.Uni;
import org.mabartos.meetmethere.interaction.rest.api.UserResource;
import org.mabartos.meetmethere.dto.User;
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
    public Uni<User> getUser() {
        return null;
    }

    @Override
    public void removeUser() {

    }

    @Override
    public Uni<User> updateUser(User user) {
        return null;
    }
}
