package org.mabartos.meetmethere.service.rest;

import io.smallrye.mutiny.Uni;
import org.mabartos.meetmethere.interaction.rest.api.UserResource;
import org.mabartos.meetmethere.api.model.UserModel;
import org.mabartos.meetmethere.api.model.exception.ModelNotFoundException;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;
import org.mabartos.meetmethere.interaction.rest.api.model.ModelToJson;
import org.mabartos.meetmethere.interaction.rest.api.model.UserJson;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.mabartos.meetmethere.api.model.ModelUpdater.updateModel;
import static org.mabartos.meetmethere.interaction.rest.api.model.ModelToJson.toJson;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class UserResourceProvider implements UserResource {
    private final MeetMeThereSession session;
    private final UserModel user;

    public UserResourceProvider(MeetMeThereSession session, UserModel user) {
        this.session = session;
        if (user == null) throw new NotFoundException("Cannot find user");
        this.user = user;
    }

    @Override
    public Uni<UserJson> getUser() {
        return Uni.createFrom().item(ModelToJson.toJson(user));
    }

    @Override
    public Response removeUser() {
        try {
            session.users().removeUser(user.getId());
            return Response.noContent().build();
        } catch (ModelNotFoundException e) {
            throw new NotFoundException("Cannot remove user. User doesn't exist.");
        }
    }

    @Override
    public Uni<UserJson> updateUser(UserJson user) {
        try {
            updateModel(user, this.user);
            session.users().updateUser(this.user);
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("Cannot update user. User doesn't exist.");
        }

        return Uni.createFrom()
                .item(ModelToJson.toJson(session.users().getUserById(this.user.getId())));
    }
}
