package org.mabartos.meetmethere.model.jpa.adapter;

import io.smallrye.common.constraint.NotNull;
import org.mabartos.meetmethere.api.model.EventModel;
import org.mabartos.meetmethere.api.model.HasId;
import org.mabartos.meetmethere.api.model.UserModel;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;
import org.mabartos.meetmethere.model.jpa.JpaModel;
import org.mabartos.meetmethere.model.jpa.entity.UserEntity;
import org.mabartos.meetmethere.model.jpa.util.JpaUtil;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Deprecated
public class JpaUserAdapter implements UserModel, JpaModel<UserEntity> {
    private final MeetMeThereSession session;
    private final EntityManager em;
    private final UserEntity entity;

    public JpaUserAdapter(MeetMeThereSession session,
                          @NotNull EntityManager em,
                          @NotNull UserEntity entity) {
        this.session = session;
        this.em = em;
        this.entity = entity;
    }

    @Override
    public String getId() {
        return Long.toString(getEntity().getId());
    }

    @Override
    public void setId(String id) {
        try {
            getEntity().setId(Long.parseLong(id));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The ID must be Long");
        }
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
                .map(f -> session.eventStorage().getEventById(f))
                .collect(Collectors.toSet());
    }

    @Override
    public void setOrganizedEvents(Set<EventModel> events) {
        final Set<Long> entities = events.stream()
                .filter(Objects::nonNull)
                .map(HasId::getId)
                .collect(Collectors.toSet());

        getEntity().setOrganizedEvents(entities);
    }

    @Override
    public Set<String> getRoles() {
        return Collections.emptySet();
    }

    @Override
    public void setRoles(Set<String> roles) {

    }

    @Override
    public UserEntity getEntity() {
        return entity;
    }

    @Override
    public void setAttribute(String key, String value) {
        getEntity().getAttributes().put(key, value);
    }

    @Override
    public void removeAttribute(String name) {
        getEntity().getAttributes().remove(name);
    }

    @Override
    public Map<String, String> getAttributes() {
        return getEntity().getAttributes();
    }

    @Override
    public void setAttributes(Map<String, String> attributes) {
        getEntity().setAttributes(attributes);
    }

    public static UserEntity convertToEntity(UserModel userModel, EntityManager em) {
        return JpaUtil.convertToEntity(userModel, em, JpaUserAdapter.class, UserEntity.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JpaUserAdapter)) return false;
        JpaUserAdapter that = (JpaUserAdapter) o;
        return Objects.equals(getEntity(), that.getEntity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEntity());
    }
}
