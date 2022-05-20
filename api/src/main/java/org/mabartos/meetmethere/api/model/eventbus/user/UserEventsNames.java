package org.mabartos.meetmethere.api.model.eventbus.user;

public interface UserEventsNames {
    String USER_CREATE_EVENT = "userCreateEvent";
    String USER_CREATE_BASIC_EVENT = "userCreateBasicEvent";
    String USER_UPDATE_EVENT = "userUpdateEvent";
    String USER_REMOVE_EVENT = "userRemoveEvent";
    String USER_GET_USERS_EVENT = "userGetMultipleEvent";
    String USER_GET_USER_EVENT = "userGetSingleEvent";
    String USER_GET_USERNAME_EVENT = "userGetByUsernameEvent";
    String USER_GET_EMAIL_EVENT = "userGetByEmailEvent";
    String USER_COUNT_EVENT = "userCountEvent";
}
