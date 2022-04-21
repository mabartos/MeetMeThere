package org.mabartos.meetmethere.api.model.eventbus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailUsernameObject {
    private String email;
    private String username;
}
