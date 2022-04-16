package org.mabartos.meetmethere.service.rest;

import io.smallrye.mutiny.Uni;
import org.mabartos.meetmethere.dto.User;
import org.mabartos.meetmethere.interaction.rest.api.UserResource;
import org.mabartos.meetmethere.model.UserModel;
import org.mabartos.meetmethere.model.exception.ModelNotFoundException;
import org.mabartos.meetmethere.session.MeetMeThereSession;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.mabartos.meetmethere.DtoToModel.updateModel;
import static org.mabartos.meetmethere.ModelToDto.toDto;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class UserResourceProvider implements UserResource {
    private final MeetMeThereSession session;
    private final UserModel user;

    public UserResourceProvider(MeetMeThereSession session, UserModel user) {
        this.session = session;
        this.user = user;
    }

    @Override
    public Uni<User> getUser() {
        return Uni.createFrom().item(toDto(user));
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
    public Uni<User> updateUser(User user) {
        try {
            updateModel(user, this.user);
            session.users().updateUser(this.user);
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("Cannot update user. User doesn't exist.");
        }

        return Uni.createFrom()
                .item(toDto(session.users().getUserById(this.user.getId())));
    }
}
