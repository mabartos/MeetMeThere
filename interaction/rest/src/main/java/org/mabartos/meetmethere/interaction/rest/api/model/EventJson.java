package org.mabartos.meetmethere.interaction.rest.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.mabartos.meetmethere.api.domain.Event;
import org.mabartos.meetmethere.api.domain.User;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventJson extends Event {

    @JsonCreator
    public EventJson(String title, User createdBy) {
        super(title,createdBy);
    }

}
