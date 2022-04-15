package org.mabartos.meetmethere.model;

import java.util.Map;

public interface HasAttributes<Key, Value> {
    void setAttribute(Key key, Value value);

    void removeAttribute(Key name);

    Map<Key, Value> getAttributes();

    void setAttributes(Map<Key, Value> attributes);
}
