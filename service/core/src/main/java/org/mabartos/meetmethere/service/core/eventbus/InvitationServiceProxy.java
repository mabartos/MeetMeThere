package org.mabartos.meetmethere.service.core.eventbus;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Uni;
import org.mabartos.meetmethere.api.domain.EventInvitation;
import org.mabartos.meetmethere.api.model.eventbus.invitation.EventInvitationSet;
import org.mabartos.meetmethere.api.service.EventInvitationService;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import static org.mabartos.meetmethere.api.model.eventbus.invitation.InvitationEventsNames.EVENT_INVITE_COUNT_EVENT;
import static org.mabartos.meetmethere.api.model.eventbus.invitation.InvitationEventsNames.EVENT_INVITE_CREATE_EVENT;
import static org.mabartos.meetmethere.api.model.eventbus.invitation.InvitationEventsNames.EVENT_INVITE_GET_MULTIPLE_EVENT;
import static org.mabartos.meetmethere.api.model.eventbus.invitation.InvitationEventsNames.EVENT_INVITE_GET_SINGLE_EVENT;
import static org.mabartos.meetmethere.api.model.eventbus.invitation.InvitationEventsNames.EVENT_INVITE_REMOVE_MULTIPLE_EVENT;
import static org.mabartos.meetmethere.api.model.eventbus.invitation.InvitationEventsNames.EVENT_INVITE_REMOVE_SINGLE_EVENT;
import static org.mabartos.meetmethere.api.model.eventbus.invitation.InvitationEventsNames.EVENT_INVITE_UPDATE_EVENT;

@ApplicationScoped
@Transactional
public class InvitationServiceProxy {
    private final MeetMeThereSession session;
    private final EventInvitationService delegate;

    @Inject
    public InvitationServiceProxy(MeetMeThereSession session, EventInvitationService delegate) {
        this.session = session;
        this.delegate = delegate;
    }

    @ConsumeEvent(value = EVENT_INVITE_GET_MULTIPLE_EVENT, blocking = true)
    public Uni<EventInvitationSet> getInvitations(Long eventId) {
        return delegate.getInvitations(eventId).onItem().transform(EventInvitationSet::new);
    }

    @ConsumeEvent(value = EVENT_INVITE_CREATE_EVENT, blocking = true)
    public Uni<Long> createInvitation(EventInvitation invitation) {
        return delegate.createInvitation(invitation);
    }

    @ConsumeEvent(value = EVENT_INVITE_REMOVE_MULTIPLE_EVENT, blocking = true)
    public void removeInvitations(Long eventId) {
        delegate.removeInvitations(eventId);
    }

    @ConsumeEvent(value = EVENT_INVITE_COUNT_EVENT, blocking = true)
    public Uni<Long> getInvitationsCount(Long eventId) {
        return delegate.getInvitationsCount(eventId);
    }

    @ConsumeEvent(value = EVENT_INVITE_GET_SINGLE_EVENT, blocking = true)
    public Uni<EventInvitation> getInvitation(Long invitationId) {
        return delegate.getInvitation(invitationId);
    }

    @ConsumeEvent(value = EVENT_INVITE_REMOVE_SINGLE_EVENT, blocking = true)
    public void removeInvitation(Long invitationId) {
        delegate.removeInvitation(invitationId);
    }

    @ConsumeEvent(value = EVENT_INVITE_UPDATE_EVENT, blocking = true)
    public Uni<EventInvitation> updateInvitation(EventInvitation invitation) {
        return delegate.updateInvitation(invitation);
    }
}
