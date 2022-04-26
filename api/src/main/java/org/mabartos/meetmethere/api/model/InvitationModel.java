package org.mabartos.meetmethere.api.model;

public interface InvitationModel extends HasId<Long> {

    EventModel getEvent();

    void setEvent(EventModel event);

    UserModel getSender();

    void setSender(UserModel sender);

    UserModel getReceiver();

    void setReceiver(UserModel receiver);

    String getMessage();

    void setMessage(String message);
}
