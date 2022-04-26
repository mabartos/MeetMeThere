package org.mabartos.meetmethere.api.domain;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Data
public class User {

    private Long id;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private Set<Long> organizedEventsId;

    private Map<String, String> attributes;

    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.organizedEventsId = new HashSet<>();
        this.attributes = new HashMap<>();
    }

    public String getFullName() {
        return String.format("%s %s", firstName, lastName);
    }
}
