package org.mabartos.meetmethere.dto;

import lombok.Getter;
import lombok.Setter;
import org.mabartos.meetmethere.enums.ResponseType;

@Setter
@Getter
public class EventInvitation {

    private Event event;

    private User sender;

    private User receiver;

    private ResponseType responseType;

    public EventInvitation(Event event, User sender, User receiver) {
        this.event = event;
        this.sender = sender;
        this.receiver = receiver;
        this.responseType = ResponseType.NOT_ANSWERED;
    }
}
