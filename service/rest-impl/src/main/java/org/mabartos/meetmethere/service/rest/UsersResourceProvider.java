package org.mabartos.meetmethere.service.rest;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.mabartos.meetmethere.interaction.rest.api.UserResource;
import org.mabartos.meetmethere.interaction.rest.api.UsersResource;
import org.mabartos.meetmethere.dto.User;
import org.mabartos.meetmethere.model.UserModel;
import org.mabartos.meetmethere.session.MeetMeThereSession;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import static org.mabartos.meetmethere.interaction.rest.api.ResourceConstants.FIRST_RESULT;
import static org.mabartos.meetmethere.interaction.rest.api.ResourceConstants.ID;
import static org.mabartos.meetmethere.interaction.rest.api.ResourceConstants.MAX_RESULTS;

//TODO
@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsersResourceProvider implements UsersResource {

    @Context
    MeetMeThereSession session;

    @GET
    @Path("/{id}")
    public UserResource getUserById(@PathParam(ID) Long id) {
        final UserModel user = session.users().getUserById(id);

        if (user == null) {
            throw new NotFoundException("Cannot find user with id: " + id);
        }

        return new UserResourceProvider(session, user);
    }

    @GET
    @Path("/{username}")
    public UserResource getUserByUsername(@PathParam("username") String username) {
        final UserModel user = session.users().getUserByUsername(username);

        if (user == null) {
            throw new NotFoundException("Cannot find user with username: " + username);
        }

        return new UserResourceProvider(session, user);
    }

    @GET
    public Multi<User> getUsers(@QueryParam(FIRST_RESULT) int firstResult, @QueryParam(MAX_RESULTS) int maxResults) {
        return null;
        //return session.users().getUsers(firstResult, maxResults);
    }

    @GET
    @Path("/count")
    public Uni<Long> getUsersCount() {
        return Uni.createFrom().item(session.users().getUsersCount());
    }

    @POST
    public Uni<User> createUser(User user) {
        return null;
        //return session.users().createUser(user);
    }
}