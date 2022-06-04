package org.mabartos.meetmethere.api.model;

import java.util.Set;

public interface UserModel extends HasAttributes<String, String>, HasId<String> {

    String getUsername();

    void setUsername(String username);

    String getFirstName();

    void setFirstName(String firstName);

    String getLastName();

    void setLastName(String lastName);

    String getEmail();

    void setEmail(String email);

    Set<EventModel> getOrganizedEvents();

    void setOrganizedEvents(Set<EventModel> events);

    Set<String> getRoles();

    void setRoles(Set<String> roles);
}
