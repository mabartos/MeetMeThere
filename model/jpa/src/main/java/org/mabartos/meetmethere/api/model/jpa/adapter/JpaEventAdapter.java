package org.mabartos.meetmethere.api.model.jpa.adapter;

import io.smallrye.common.constraint.NotNull;
import org.mabartos.meetmethere.api.enums.ResponseType;
import org.mabartos.meetmethere.api.model.AddressModel;
import org.mabartos.meetmethere.api.model.EventModel;
import org.mabartos.meetmethere.api.model.InvitationModel;
import org.mabartos.meetmethere.api.model.UserModel;
import org.mabartos.meetmethere.api.model.jpa.JpaModel;
import org.mabartos.meetmethere.api.model.jpa.entity.EventEntity;
import org.mabartos.meetmethere.api.model.jpa.entity.UserEntity;
import org.mabartos.meetmethere.api.model.jpa.util.JpaUtil;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class JpaEventAdapter implements EventModel, JpaModel<EventEntity> {
    private final MeetMeThereSession session;
    private final EntityManager em;
    private final EventEntity entity;

    public JpaEventAdapter(MeetMeThereSession session,
                           @NotNull EntityManager em,
                           @NotNull EventEntity entity) {
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
    public String getEventTitle() {
        return getEntity().getTitle();
    }

    @Override
    public void setEventTitle(String title) {
        getEntity().setTitle(title);
    }

    @Override
    public String getDescription() {
        return getEntity().getDescription();
    }

    @Override
    public void setDescription(String description) {
        getEntity().setDescription(description);
    }

    @Override
    public UserModel getCreatedBy() {
        return new JpaUserAdapter(session, em, getEntity().getCreator());
    }

    @Override
    public void setCreatedBy(UserModel user) {
        getEntity().setCreator(JpaUserAdapter.convertToEntity(user, em));
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return getEntity().getCreatedAt();
    }

    @Override
    public boolean isPublic() {
        return getEntity().isPublic();
    }

    @Override
    public void setPublic(boolean isPublic) {
        getEntity().setPublic(isPublic);
    }

    @Override
    public Set<UserModel> getOrganizers() {
        return getEntity().getOrganizers()
                .stream()
                .map(u -> new JpaUserAdapter(session, em, u))
                .collect(Collectors.toSet());
    }

    @Override
    public void setOrganizers(Set<UserModel> organizers) {
        Set<UserEntity> entities = organizers.stream()
                .filter(Objects::nonNull)
                .map(f -> JpaUserAdapter.convertToEntity(f, em))
                .collect(Collectors.toSet());

        getEntity().setOrganizers(entities);
    }

    @Override
    public LocalDateTime getStartTime() {
        return getEntity().getStartTime();
    }

    @Override
    public void setStartTime(LocalDateTime startTime) {
        getEntity().setStartTime(startTime);
    }

    @Override
    public LocalDateTime getEndTime() {
        return getEntity().getEndTime();
    }

    @Override
    public void setEndTime(LocalDateTime endTime) {
        getEntity().setEndTime(endTime);
    }

    @Override
    public AddressModel getVenue() {
        return new JpaAddressAdapter(getEntity().getVenue());
    }

    @Override
    public void setVenue(AddressModel venue) {
        getEntity().setVenue(JpaAddressAdapter.convertToEntity(venue));
    }

    @Override
    public LocalDateTime getUpdatedAt() {
        return getEntity().getUpdatedAt();
    }

    @Override
    public void setUpdatedAt(LocalDateTime updatedAt) {
        getEntity().setUpdatedAt(updatedAt);
    }

    @Override
    public UserModel getUpdatedBy() {
        return new JpaUserAdapter(session, em, getEntity().getUpdatedBy());
    }

    @Override
    public void setUpdatedBy(UserModel user) {
        getEntity().setUpdatedBy(JpaUserAdapter.convertToEntity(user, em));
    }

    @Override
    public Map<UserModel, ResponseType> getResponses() {
        final Map<UserModel, ResponseType> map = new HashMap<>();

        getEntity().getInvitations()
                .forEach(f -> map.put(new JpaUserAdapter(session, em, f.getReceiver()), f.getResponseType()));

        return map;
    }

    @Override
    public Set<InvitationModel> getInvitations() {
        return getEntity().getInvitations()
                .stream()
                .map(f -> new JpaInvitationAdapter(session, em, f))
                .collect(Collectors.toSet());
    }

    @Override
    public EventEntity getEntity() {
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

    public static EventEntity convertToEntity(EventModel model, EntityManager em) {
        return JpaUtil.convertToEntity(model, em, JpaEventAdapter.class, EventEntity.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JpaEventAdapter)) return false;
        JpaEventAdapter that = (JpaEventAdapter) o;
        return Objects.equals(getEntity(), that.getEntity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEntity());
    }
}
