package org.mabartos.meetmethere.service.rest;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.mabartos.meetmethere.ModelToDto;
import org.mabartos.meetmethere.dto.EventInvitation;
import org.mabartos.meetmethere.interaction.rest.api.EventInvitationResource;
import org.mabartos.meetmethere.interaction.rest.api.EventInvitationsResource;
import org.mabartos.meetmethere.model.EventModel;
import org.mabartos.meetmethere.model.InvitationModel;
import org.mabartos.meetmethere.model.UserModel;
import org.mabartos.meetmethere.session.MeetMeThereSession;

import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
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

import static org.mabartos.meetmethere.DtoToModel.updateModel;
import static org.mabartos.meetmethere.ModelToDto.toDto;
import static org.mabartos.meetmethere.interaction.rest.api.ResourceConstants.FIRST_RESULT;
import static org.mabartos.meetmethere.interaction.rest.api.ResourceConstants.ID;
import static org.mabartos.meetmethere.interaction.rest.api.ResourceConstants.MAX_RESULTS;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class EventInvitationsResourceProvider implements EventInvitationsResource {
    private final MeetMeThereSession session;
    private final EventModel event;

    public EventInvitationsResourceProvider(MeetMeThereSession session, EventModel event) {
        this.session = session;
        this.event = event;
    }

    @GET
    public Multi<EventInvitation> getInvitations(@QueryParam(FIRST_RESULT) int firstResult,
                                                 @QueryParam(MAX_RESULTS) int maxResults) {
        return Multi.createFrom()
                .items(session.invitations()
                        .getInvitationsForEvent(event.getId())
                        .stream()
                        .map(ModelToDto::toDto)
                        .distinct()
                        .toArray())
                .onItem()
                .castTo(EventInvitation.class);
    }

    @POST
    public Uni<EventInvitation> createInvitation(EventInvitation invitation) {
        if (session.invitations().getInvitationById(invitation.getId()) != null) {
            throw new BadRequestException("Invitation already exist");
        }

        final UserModel sender = session.users().getUserById(invitation.getSenderId());
        final UserModel receiver = session.users().getUserById(invitation.getReceiverId());

        if (sender == null || receiver == null) throw new BadRequestException("Cannot find a particular users");

        InvitationModel model = session.invitations().createInvitation(event, sender, receiver);

        updateModel(invitation, model);

        return Uni.createFrom().item(session.invitations().updateInvitation(model)).map(ModelToDto::toDto);
    }

    @POST
    public Uni<EventInvitation> sendInvitation(Long receiverId) {
        final UserModel sender = null; // TODO principal
        final UserModel receiver = session.users().getUserById(receiverId);

        return Uni.createFrom().item(toDto(session.invitations().createInvitation(event, sender, receiver)));
    }

    @DELETE
    public Response removeInvitations() {
        try {
            event.getInvitations().clear();
        } catch (Exception e) {
            return Response.status(400, "Cannot remove invitations").build();
        }
        return Response.ok().build();
    }

    @Path("/{id}")
    public EventInvitationResource getInvitationById(@PathParam(ID) Long id) {
        final InvitationModel invitation = event.getInvitations().stream().filter(f -> f.getId().equals(id)).findFirst().orElse(null);
        return new EventInvitationResourceProvider(session, invitation);
    }

    @Override
    public Uni<Long> getInvitationsCount() {
        return Uni.createFrom().item(Integer.toUnsignedLong(event.getInvitations().size()));
    }

}
