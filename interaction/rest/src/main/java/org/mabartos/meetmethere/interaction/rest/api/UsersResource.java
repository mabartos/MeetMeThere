package org.mabartos.meetmethere.interaction.rest.api;

import org.mabartos.meetmethere.interaction.rest.api.dto.User;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Set;

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
    Set<User> getUsers(@QueryParam(FIRST_RESULT) int firstResult, @QueryParam(MAX_RESULTS) int maxResults);

    @GET
    @Path("/count")
    long getUsersCount();

    @POST
    User createUser(User user);
}
