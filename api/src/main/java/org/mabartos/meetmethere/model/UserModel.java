package org.mabartos.meetmethere.model;

import java.util.List;
import java.util.Map;

public interface UserModel {

    String getUsername();

    void setUsername(String username);

    String getFirstName();

    void setFirstName(String firstName);

    String getLastName();

    void setLastName();

    String getEmail();

    void setEmail(String email);

    void setSingleAttribute(String key, String value);

    void setAttribute(String name, List<String> values);

    void removeAttribute(String name);

    Map<String, List<String>> getAttributes();
}
