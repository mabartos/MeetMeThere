package org.mabartos.meetmethere.interaction.rest.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.mabartos.meetmethere.api.domain.User;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserJson extends User {

    @JsonCreator
    public UserJson(String username, String email) {
        super(username, email);
    }

}
