package org.mabartos.meetmethere.api.model.eventbus.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailUsernameObject {
    private String email;
    private String username;
}
