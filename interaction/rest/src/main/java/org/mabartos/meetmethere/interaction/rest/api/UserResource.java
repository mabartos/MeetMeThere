package org.mabartos.meetmethere.interaction.rest.api;

import io.smallrye.mutiny.Uni;
import org.mabartos.meetmethere.dto.User;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface UserResource {

    @GET
    Uni<User> getUser();

    @DELETE
    void removeUser();

    @PATCH
    Uni<User> updateUser(User user);
}
