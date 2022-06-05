package org.mabartos.meetmethere.interaction.rest.api;

import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.mabartos.meetmethere.interaction.rest.api.model.UserJson;

import javax.annotation.security.PermitAll;
import javax.transaction.Transactional;
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
@Transactional
@Tag(name = "Users Resource API", description = "Provide API for all users.")
@Authenticated
public interface UsersResource {

    @Path("/{id}")
    UserResource getUserById(@PathParam(ID) String id);

    @GET
    @Path("/username/{username}")
    Uni<UserJson> getUserByUsername(@PathParam("username") String username);

    @GET
    @Path("/email/{email}")
    Uni<UserJson> getUserByEmail(@PathParam("email") String email);

    @GET
    Uni<Set<UserJson>> getUsers(@QueryParam(FIRST_RESULT) Integer firstResult,
                                @QueryParam(MAX_RESULTS) Integer maxResults);

    @GET
    @Path("/count")
    Uni<Long> getUsersCount();

    @Deprecated
    @POST
    @PermitAll
    Uni<Long> createUser(UserJson user);
}
