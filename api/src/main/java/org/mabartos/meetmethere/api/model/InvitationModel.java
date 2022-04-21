package org.mabartos.meetmethere.api.model;

import org.mabartos.meetmethere.api.enums.ResponseType;

public interface InvitationModel extends HasId<Long> {

    EventModel getEvent();

    void setEvent(EventModel event);

    UserModel getSender();

    void setSender(UserModel sender);

    UserModel getReceiver();

    void setReceiver(UserModel receiver);

    ResponseType getResponseType();

    void setResponseType(ResponseType responseType);

    String getMessage();

    void setMessage(String message);
}
