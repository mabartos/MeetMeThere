package org.mabartos.meetmethere.model;

import org.mabartos.meetmethere.enums.ResponseType;

public interface InvitationModel extends HasId{

    EventModel getEvent();

    void setEvent(EventModel event);

    UserModel getSender();

    void setSender(UserModel sender);

    UserModel getReceiver();

    void setReceiver(UserModel receiver);

    ResponseType getResponseType();

    void setResponseType(ResponseType responseType);
}
