package org.mabartos.meetmethere.api.service;

import io.smallrye.mutiny.Uni;
import org.mabartos.meetmethere.api.domain.EventInvitation;
import org.mabartos.meetmethere.api.model.eventbus.EventInvitationSet;

public interface EventInvitationService {
    String EVENT_INVITE_CREATE_EVENT = "eventInviteCreateEvent";
    String EVENT_INVITE_UPDATE_EVENT = "eventInviteUpdateEvent";
    String EVENT_INVITE_REMOVE_MULTIPLE_EVENT = "eventInviteRemoveMultipleEvent";
    String EVENT_INVITE_REMOVE_SINGLE_EVENT = "eventInviteRemoveSingleEvent";
    String EVENT_INVITE_GET_MULTIPLE_EVENT = "eventInviteGetMultipleEvent";
    String EVENT_INVITE_GET_SINGLE_EVENT = "eventInviteGetSingleEvent";
    String EVENT_INVITE_COUNT_EVENT = "eventInviteCountEvent";

    Uni<EventInvitationSet> getInvitations(Long eventId);

    Uni<Long> createInvitation(EventInvitation invitation);

    void removeInvitations(Long eventId);

    Uni<Long> getInvitationsCount(Long eventId);

    Uni<EventInvitation> getInvitation(Long invitationId);

    void removeInvitation(Long invitationId);

    Uni<EventInvitation> updateInvitation(EventInvitation invitation);
}
