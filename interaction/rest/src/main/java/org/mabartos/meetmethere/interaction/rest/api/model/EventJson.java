package org.mabartos.meetmethere.interaction.rest.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.mabartos.meetmethere.api.domain.Event;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventJson extends Event {

    @JsonCreator
    public EventJson(@JsonProperty("title") String title,
                     @JsonProperty("createdById") Long createdById,
                     @JsonProperty("createdByFullName") String createdByFullName) {
        super(title, createdById, createdByFullName);
    }

}
