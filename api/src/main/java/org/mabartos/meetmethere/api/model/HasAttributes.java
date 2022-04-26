package org.mabartos.meetmethere.api.model;

import java.util.Map;

public interface HasAttributes<Key, Value> {
    default void setAttribute(Key key, Value value) {
        getAttributes().put(key, value);
    }

    default void removeAttribute(Key name) {
        getAttributes().remove(name);
    }

    Map<Key, Value> getAttributes();

    void setAttributes(Map<Key, Value> attributes);
}
