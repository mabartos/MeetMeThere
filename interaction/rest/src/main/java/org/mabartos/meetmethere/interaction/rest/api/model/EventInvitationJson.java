package org.mabartos.meetmethere.interaction.rest.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.mabartos.meetmethere.api.domain.Event;
import org.mabartos.meetmethere.api.domain.EventInvitation;
import org.mabartos.meetmethere.api.domain.User;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventInvitationJson extends EventInvitation {

    @JsonCreator
    public EventInvitationJson(Event event, User sender, User receiver) {
        super(event, sender, receiver);
    }

    @JsonCreator
    public EventInvitationJson(Event event, User sender, User receiver, String message) {
        super(event, sender, receiver, message);
    }
}
