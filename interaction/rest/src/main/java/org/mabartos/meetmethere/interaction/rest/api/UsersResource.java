package org.mabartos.meetmethere.interaction.rest.api;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.mabartos.meetmethere.api.domain.User;
import org.mabartos.meetmethere.interaction.rest.api.model.UserJson;

import javax.transaction.Transactional;
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
@Transactional
public interface UsersResource {

    @Path("/{id}")
    UserResource getUserById(@PathParam(ID) Long id);

    @Path("/{username}")
    UserResource getUserByUsername(@PathParam("username") String username);

    @GET
    Multi<UserJson> getUsers(@QueryParam(FIRST_RESULT) Integer firstResult,
                             @QueryParam(MAX_RESULTS) Integer maxResults);

    @GET
    @Path("/count")
    Uni<Long> getUsersCount();

    @POST
    Uni<UserJson> createUser(UserJson user);
}
