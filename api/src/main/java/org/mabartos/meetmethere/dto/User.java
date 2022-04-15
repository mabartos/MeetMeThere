package org.mabartos.meetmethere.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class User {

    private Long id;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private Set<Event> organizedEvents;

    private Map<String, String> attributes;

    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.organizedEvents = new HashSet<>();
        this.attributes = new HashMap<>();
    }
}
