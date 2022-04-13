package org.mabartos.meetmethere.model.jpa.entity.attribute;

import java.util.List;

public interface HasAttributesEntity<Entity extends AttributeContent> {
    List<Entity> getAttributes();
}
