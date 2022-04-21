package org.mabartos.meetmethere.interaction.rest.api;

import io.smallrye.mutiny.Uni;
import org.mabartos.meetmethere.api.domain.EventInvitation;
import org.mabartos.meetmethere.interaction.rest.api.model.EventInvitationJson;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public interface EventInvitationResource {

    @GET
    Uni<EventInvitationJson> getInvitation();

    @GET
    @Path("/accept")
    Uni<EventInvitationJson> acceptInvitation();

    @GET
    @Path("/decline")
    Uni<EventInvitationJson> declineInvitation();

    @GET
    @Path("/maybe")
    Uni<EventInvitationJson> addMaybeStatus();

    @DELETE
    Response removeInvitation();

    @PATCH
    Uni<EventInvitationJson> updateInvitation(EventInvitationJson invitation);
}
