package org.mabartos.meetmethere.service.rest;

import io.smallrye.mutiny.Uni;
import org.mabartos.meetmethere.api.model.UserModel;
import org.mabartos.meetmethere.api.service.UserService;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;
import org.mabartos.meetmethere.interaction.rest.api.UserResource;
import org.mabartos.meetmethere.interaction.rest.api.model.ModelToJson;
import org.mabartos.meetmethere.interaction.rest.api.model.UserJson;

import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.mabartos.meetmethere.service.rest.UsersResourceProvider.getSingleUser;

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
        session.eventBus().publish(UserService.USER_REMOVE_EVENT, user.getId());
        return Response.ok().build();
    }

    @Override
    public Uni<UserJson> updateUser(UserJson user) {
        if (user.getId() != null && !this.user.getId().equals(user.getId())) {
            throw new BadRequestException("Cannot update Event - different IDs");
        }
        user.setId(this.user.getId());

        return getSingleUser(session.eventBus(), UserService.USER_UPDATE_EVENT, user);
    }
}
