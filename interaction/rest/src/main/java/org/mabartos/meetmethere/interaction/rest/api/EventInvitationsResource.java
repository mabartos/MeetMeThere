package org.mabartos.meetmethere.interaction.rest.api;

import org.mabartos.meetmethere.dto.EventInvitation;

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
import java.util.Set;

import static org.mabartos.meetmethere.interaction.rest.api.ResourceConstants.FIRST_RESULT;
import static org.mabartos.meetmethere.interaction.rest.api.ResourceConstants.ID;
import static org.mabartos.meetmethere.interaction.rest.api.ResourceConstants.MAX_RESULTS;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public interface EventInvitationsResource {

    @GET
    Set<EventInvitation> getInvitations(@QueryParam(FIRST_RESULT) int firstResult, @QueryParam(MAX_RESULTS) int maxResults);

    @POST
    EventInvitation createInvitation(EventInvitation invitation);

    @POST
    EventInvitation sendInvitation(Long receiverId);

    @DELETE
    void removeInvitations();

    @GET
    @Path("/{id}")
    EventInvitationResource getInvitationById(@PathParam(ID) Long id);
}
