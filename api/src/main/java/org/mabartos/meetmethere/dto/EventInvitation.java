package org.mabartos.meetmethere.dto;

import lombok.Getter;
import lombok.Setter;
import org.mabartos.meetmethere.enums.ResponseType;

import java.io.Serializable;

@Setter
@Getter
public class EventInvitation implements Serializable {

    private String eventTitle;

    private Long eventId;

    private String senderName;

    private Long senderId;

    private String receiverName;

    private Long receiverId;

    private String message;

    private ResponseType responseType;

    public EventInvitation(Event event, User sender, User receiver) {
        this.eventTitle = event.getTitle();
        this.eventId = event.getId();
        this.senderName = sender.getFullName();
        this.senderId = sender.getId();
        this.receiverName = receiver.getFullName();
        this.receiverId = receiver.getId();
        this.responseType = ResponseType.NOT_ANSWERED;
    }

    public EventInvitation(Event event, User sender, User receiver, String message) {
        this(event, sender, receiver);
        this.message = message;
    }
}
