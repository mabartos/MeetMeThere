package org.mabartos.meetmethere.interaction.rest.api;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.mabartos.meetmethere.dto.User;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import static org.mabartos.meetmethere.interaction.rest.api.ResourceConstants.FIRST_RESULT;
import static org.mabartos.meetmethere.interaction.rest.api.ResourceConstants.ID;
import static org.mabartos.meetmethere.interaction.rest.api.ResourceConstants.MAX_RESULTS;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface UsersResource {

    @GET
    @Path("/{id}")
    UserResource getUserById(@PathParam(ID) Long id);

    @GET
    @Path("/{username}")
    UserResource getUserByUsername(@PathParam("username") String username);

    @GET
    Multi<User> getUsers(@QueryParam(FIRST_RESULT) int firstResult, @QueryParam(MAX_RESULTS) int maxResults);

    @GET
    @Path("/count")
    Uni<Long> getUsersCount();

    @POST
    Uni<User> createUser(User user);
}
