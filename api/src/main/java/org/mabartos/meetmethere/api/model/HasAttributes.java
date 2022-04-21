package org.mabartos.meetmethere.api.model;

import java.util.Map;

public interface HasAttributes<Key, Value> {
    void setAttribute(Key key, Value value);

    void removeAttribute(Key name);

    Map<Key, Value> getAttributes();

    void setAttributes(Map<Key, Value> attributes);
}
