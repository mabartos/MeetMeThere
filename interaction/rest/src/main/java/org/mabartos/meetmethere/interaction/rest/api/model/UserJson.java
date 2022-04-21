package org.mabartos.meetmethere.interaction.rest.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.mabartos.meetmethere.api.domain.User;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserJson extends User {

    @JsonIgnore
    private Set<Long> organizedEventsId;

    @JsonCreator
    public UserJson(@JsonProperty("username") String username,
                    @JsonProperty("email") String email) {
        super(username, email);
    }

}
