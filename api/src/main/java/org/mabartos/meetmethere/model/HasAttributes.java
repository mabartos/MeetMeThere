package org.mabartos.meetmethere.model;

import java.util.Map;

public interface HasAttributes {
    void setAttribute(String key, String value);

    void removeAttribute(String name);

    Map<String, String> getAttributes();

    void setAttributes(Map<String, String> attributes);
}
