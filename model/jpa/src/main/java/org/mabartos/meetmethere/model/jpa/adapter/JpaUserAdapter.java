package org.mabartos.meetmethere.model.jpa.adapter;

import javax.persistence.EntityManager;
import org.mabartos.meetmethere.model.EventModel;
import org.mabartos.meetmethere.model.UserModel;
import org.mabartos.meetmethere.model.jpa.JpaModel;
import org.mabartos.meetmethere.model.jpa.entity.EventEntity;
import org.mabartos.meetmethere.model.jpa.entity.UserEntity;
import org.mabartos.meetmethere.session.MeetMeThereSession;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class JpaUserAdapter implements UserModel, JpaModel<UserEntity> {
    private final MeetMeThereSession session;
    private final EntityManager em;
    private final UserEntity entity;

    public JpaUserAdapter(MeetMeThereSession session, EntityManager em, UserEntity entity) {
        this.session = session;
        this.em = em;
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
    public Set<EventModel> getOrganizedEvents() {
        return getEntity().getOrganizedEvents()
                .stream()
                .filter(Objects::nonNull)
                .map(f -> new JpaEventAdapter(session, em, f))
                .collect(Collectors.toSet());
    }

    @Override
    public void setOrganizedEvents(Set<EventModel> events) {
        final Set<EventEntity> entities = events.stream()
                .filter(Objects::nonNull)
                .map(f -> EventEntity.findByIdOptional(f.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(f -> (EventEntity) f)
                .collect(Collectors.toSet());

        getEntity().setOrganizedEvents(entities);
    }

    @Override
    public UserEntity getEntity() {
        return entity;
    }

    @Override
    public void setAttribute(String key, String value) {

    }

    @Override
    public void removeAttribute(String name) {

    }

    @Override
    public Map<String, String> getAttributes() {
        return null;
    }

    @Override
    public void setAttributes(Map<String, String> attributes) {

    }
}
