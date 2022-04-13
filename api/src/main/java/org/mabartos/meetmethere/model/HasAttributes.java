package org.mabartos.meetmethere.model;

import java.util.List;
import java.util.Map;

public interface HasAttributes {
    void setSingleAttribute(String key, String value);

    void setAttribute(String name, List<String> values);

    void removeAttribute(String name);

    Map<String, List<String>> getAttributes();
}
