package org.mabartos.meetmethere.interaction.rest.api;

import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.mabartos.meetmethere.interaction.rest.api.model.EventInvitationJson;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Transactional
@Tag(name = "Event Invitation Resource API", description = "Provide API for a particular event invitation.")
@Authenticated
public interface EventInvitationResource {

    @GET
    Uni<EventInvitationJson> getInvitation();

    @DELETE
    Response removeInvitation();

    @PATCH
    Uni<EventInvitationJson> updateInvitation(EventInvitationJson invitation);
}
