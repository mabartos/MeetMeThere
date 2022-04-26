package org.mabartos.meetmethere.interaction.rest.api;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.mabartos.meetmethere.interaction.rest.api.model.EventInvitationJson;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

import static org.mabartos.meetmethere.interaction.rest.api.ResourceConstants.FIRST_RESULT;
import static org.mabartos.meetmethere.interaction.rest.api.ResourceConstants.ID;
import static org.mabartos.meetmethere.interaction.rest.api.ResourceConstants.MAX_RESULTS;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Transactional
@Tag(name = "Event Invitations Resource API", description = "Provide API for all invitations to event.")
public interface EventInvitationsResource {

    @GET
    Uni<Set<EventInvitationJson>> getInvitations();

    @POST
    Uni<Long> createInvitation(EventInvitationJson invitation);

    @DELETE
    Response removeInvitations();

    @Path("/{id}")
    EventInvitationResource getInvitationById(@PathParam(ID) Long id);

    @GET
    @Path("/count")
    Uni<Long> getInvitationsCount();
}
