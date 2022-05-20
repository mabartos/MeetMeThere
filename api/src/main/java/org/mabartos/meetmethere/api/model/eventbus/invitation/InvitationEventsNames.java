package org.mabartos.meetmethere.api.model.eventbus.invitation;

public interface InvitationEventsNames {
    String EVENT_INVITE_CREATE_EVENT = "eventInviteCreateEvent";
    String EVENT_INVITE_UPDATE_EVENT = "eventInviteUpdateEvent";
    String EVENT_INVITE_REMOVE_MULTIPLE_EVENT = "eventInviteRemoveMultipleEvent";
    String EVENT_INVITE_REMOVE_SINGLE_EVENT = "eventInviteRemoveSingleEvent";
    String EVENT_INVITE_GET_MULTIPLE_EVENT = "eventInviteGetMultipleEvent";
    String EVENT_INVITE_GET_SINGLE_EVENT = "eventInviteGetSingleEvent";
    String EVENT_INVITE_COUNT_EVENT = "eventInviteCountEvent";
}
