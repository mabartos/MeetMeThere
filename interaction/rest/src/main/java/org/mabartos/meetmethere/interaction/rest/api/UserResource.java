package org.mabartos.meetmethere.interaction.rest.api;

import org.mabartos.meetmethere.interaction.rest.api.dto.User;

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
    User getUser();

    @DELETE
    void removeUser();

    @PATCH
    User updateUser(User user);
}
