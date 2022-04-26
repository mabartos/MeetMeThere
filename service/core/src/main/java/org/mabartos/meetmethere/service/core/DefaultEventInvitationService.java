package org.mabartos.meetmethere.service.core;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Uni;
import org.mabartos.meetmethere.api.domain.EventInvitation;
import org.mabartos.meetmethere.api.mapper.ModelToDomain;
import org.mabartos.meetmethere.api.model.EventModel;
import org.mabartos.meetmethere.api.model.InvitationModel;
import org.mabartos.meetmethere.api.model.ModelUpdater;
import org.mabartos.meetmethere.api.model.UserModel;
import org.mabartos.meetmethere.api.model.eventbus.EventInvitationSet;
import org.mabartos.meetmethere.api.model.exception.ModelNotFoundException;
import org.mabartos.meetmethere.api.service.EventInvitationService;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
public class DefaultEventInvitationService implements EventInvitationService {
    private final MeetMeThereSession session;

    @Inject
    public DefaultEventInvitationService(MeetMeThereSession session) {
        this.session = session;
    }

    @Override
    @ConsumeEvent(value = EVENT_INVITE_GET_MULTIPLE_EVENT, blocking = true)
    public Uni<EventInvitationSet> getInvitations(Long eventId) {
        final Set<EventInvitation> invitationSet = session.invitationStorage()
                .getInvitationsForEvent(eventId).stream()
                .map(ModelToDomain::toDomain)
                .collect(Collectors.toSet());

        return Uni.createFrom().item(new EventInvitationSet(invitationSet));
    }

    @Override
    @ConsumeEvent(value = EVENT_INVITE_CREATE_EVENT, blocking = true)
    public Uni<Long> createInvitation(EventInvitation invitation) {
        final EventModel event = session.eventStorage().getEventById(invitation.getEventId());
        if (event == null) throw new ModelNotFoundException("Cannot create invitation. Event doesn't exist");

        final UserModel sender = session.userStorage().getUserById(invitation.getSenderId());
        if (sender == null) throw new ModelNotFoundException("Cannot create invitation. Sender doesn't exist");

        final UserModel receiver = session.userStorage().getUserById(invitation.getReceiverId());
        if (receiver == null) throw new ModelNotFoundException("Cannot create invitation. Receiver doesn't exist");

        return Uni.createFrom().item(session.invitationStorage().createInvitation(event, sender, receiver, invitation.getMessage()).getId());
    }

    @Override
    @ConsumeEvent(value = EVENT_INVITE_REMOVE_MULTIPLE_EVENT, blocking = true)
    public void removeInvitations(Long eventId) {
        final EventModel event = session.eventStorage().getEventById(eventId);
        if (event != null) {
            event.getInvitations().clear();
        }
    }

    @Override
    @ConsumeEvent(value = EVENT_INVITE_COUNT_EVENT, blocking = true)
    public Uni<Long> getInvitationsCount(Long eventId) {
        final EventModel event = session.eventStorage().getEventById(eventId);
        if (event == null) throw new ModelNotFoundException();

        return Uni.createFrom()
                .item(event.getInvitations().size())
                .onItem()
                .transform(Integer::toUnsignedLong);
    }

    @Override
    @ConsumeEvent(value = EVENT_INVITE_GET_SINGLE_EVENT, blocking = true)
    public Uni<EventInvitation> getInvitation(Long invitationId) {
        InvitationModel invitation = session.invitationStorage().getInvitationById(invitationId);
        if (invitation == null) throw new ModelNotFoundException();

        return Uni.createFrom().item(invitation).onItem().transform(ModelToDomain::toDomain);
    }

    @Override
    @ConsumeEvent(value = EVENT_INVITE_REMOVE_SINGLE_EVENT, blocking = true)
    public void removeInvitation(Long invitationId) {
        session.invitations().removeInvitation(invitationId);
    }

    @Override
    @ConsumeEvent(value = EVENT_INVITE_UPDATE_EVENT, blocking = true)
    public Uni<EventInvitation> updateInvitation(EventInvitation invitation) {
        InvitationModel model = session.invitationStorage().getInvitationById(invitation.getId());
        if (model == null) throw new ModelNotFoundException();

        ModelUpdater.updateModel(invitation, model);
        return Uni.createFrom()
                .item(session.invitationStorage().updateInvitation(model))
                .onItem()
                .transform(ModelToDomain::toDomain);
    }
}
