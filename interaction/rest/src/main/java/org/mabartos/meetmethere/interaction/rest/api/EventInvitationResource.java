package org.mabartos.meetmethere.interaction.rest.api;

import org.mabartos.meetmethere.interaction.rest.api.dto.EventInvitation;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface EventInvitationResource {

    @GET
    EventInvitation getInvitation();

    @GET
    @Path("/accept")
    EventInvitation acceptInvitation();

    @GET
    @Path("/decline")
    EventInvitation declineInvitation();

    @GET
    @Path("/maybe")
    EventInvitation addMaybeStatus();

    @DELETE
    void removeInvitation();

    @PATCH
    void updateInvitation(EventInvitation invitation);
}
