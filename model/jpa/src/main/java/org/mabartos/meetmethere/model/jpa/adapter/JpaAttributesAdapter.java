package org.mabartos.meetmethere.model.jpa.adapter;

import jakarta.persistence.EntityManager;
import org.mabartos.meetmethere.model.HasAttributes;
import org.mabartos.meetmethere.model.jpa.entity.attribute.AttributeContent;
import org.mabartos.meetmethere.model.jpa.entity.attribute.HasAttributesEntity;
import org.mabartos.meetmethere.session.MeetMeThereSession;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class JpaAttributesAdapter<Type extends AttributeContent> implements HasAttributes {
    protected final MeetMeThereSession session;
    protected final EntityManager em;
    private final HasAttributesEntity<Type> entity;

    public JpaAttributesAdapter(MeetMeThereSession session, EntityManager em, HasAttributesEntity<Type> entity) {
        this.session = session;
        this.em = em;
        this.entity = entity;
    }

    @Override
    public void setSingleAttribute(String key, String value) {
        //TODO
    }

    @Override
    public void setAttribute(String name, List<String> values) {
        //TODO

    }

    @Override
    public void removeAttribute(String name) {
        //TODO

    }

    @Override
    public Map<String, List<String>> getAttributes() {
        return Collections.emptyMap();
    }
}