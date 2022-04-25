package org.mabartos.meetmethere.service.rest;

import io.smallrye.mutiny.Uni;
import org.mabartos.meetmethere.api.enums.ResponseType;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;
import org.mabartos.meetmethere.interaction.rest.api.EventInvitationResource;
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
public class EventInvitationResourceProvider implements EventInvitationResource {
    private final MeetMeThereSession session;
    private final Long invitationId;

    public EventInvitationResourceProvider(MeetMeThereSession session, Long invitationId) {
        this.session = session;
        this.invitationId = invitationId;
    }

    @GET
    public Uni<EventInvitationJson> getInvitation() {
        //return Uni.createFrom().item(toDomain(invitation));
        return null;
    }

    @GET
    @Path("/accept")
    public Uni<EventInvitationJson> acceptInvitation() {
        return updateStatus(ResponseType.ACCEPTED);
    }

    @GET
    @Path("/decline")
    public Uni<EventInvitationJson> declineInvitation() {
        return updateStatus(ResponseType.DECLINED);
    }

    @GET
    @Path("/maybe")
    public Uni<EventInvitationJson> addMaybeStatus() {
        return updateStatus(ResponseType.MAYBE);
    }

    @DELETE
    public Response removeInvitation() {
        /*try {
            session.invitations().removeInvitation(invitation.getId());
            return Response.noContent().build();
        } catch (ModelNotFoundException e) {
            throw new NotFoundException("Cannot find invitation");
        }*/
        return null;
    }

    @PATCH
    public Uni<EventInvitationJson> updateInvitation(EventInvitationJson invitation) {
        /*updateModel(invitation, this.invitation);
        return Uni.createFrom().item(toDomain(session.invitations().updateInvitation(this.invitation)));*/
        return null;
    }

    private Uni<EventInvitationJson> updateStatus(ResponseType status) {
       /* invitation.setResponseType(status);
        InvitationModel updated = session.invitations().updateInvitation(invitation);

        return Uni.createFrom().item(toDomain(updated));*/
        return null;
    }
}
