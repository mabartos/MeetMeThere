package org.mabartos.meetmethere.model.jpa.adapter;

import jakarta.persistence.EntityManager;
import org.mabartos.meetmethere.model.UserModel;
import org.mabartos.meetmethere.model.jpa.JpaModel;
import org.mabartos.meetmethere.model.jpa.entity.UserEntity;
import org.mabartos.meetmethere.model.jpa.entity.attribute.UserAttributeEntity;
import org.mabartos.meetmethere.session.MeetMeThereSession;

public class JpaUserAdapter extends JpaAttributesAdapter<UserAttributeEntity> implements UserModel, JpaModel<UserEntity> {
    private final UserEntity entity;

    public JpaUserAdapter(MeetMeThereSession session, EntityManager em, UserEntity entity) {
        super(session, em, entity);
        this.entity = entity;
    }

    @Override
    public Long getId() {
        return getEntity().getId();
    }

    @Override
    public void setId(Long id) {
        getEntity().setId(id);
    }

    @Override
    public String getUsername() {
        return getEntity().getUsername();
    }

    @Override
    public void setUsername(String username) {
        getEntity().setUsername(username);
    }

    @Override
    public String getFirstName() {
        return getEntity().getFirstName();
    }

    @Override
    public void setFirstName(String firstName) {
        getEntity().setFirstName(firstName);
    }

    @Override
    public String getLastName() {
        return getEntity().getLastName();
    }

    @Override
    public void setLastName(String lastName) {
        getEntity().setLastName(lastName);
    }

    @Override
    public String getEmail() {
        return getEntity().getEmail();
    }

    @Override
    public void setEmail(String email) {
        getEntity().setEmail(email);
    }

    @Override
    public UserEntity getEntity() {
        return entity;
    }
}
