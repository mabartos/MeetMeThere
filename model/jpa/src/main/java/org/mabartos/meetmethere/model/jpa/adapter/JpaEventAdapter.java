package org.mabartos.meetmethere.model.jpa.adapter;

import io.smallrye.common.constraint.NotNull;
import org.mabartos.meetmethere.api.model.AddressModel;
import org.mabartos.meetmethere.api.model.EventModel;
import org.mabartos.meetmethere.api.model.HasId;
import org.mabartos.meetmethere.api.model.InvitationModel;
import org.mabartos.meetmethere.api.model.UserModel;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;
import org.mabartos.meetmethere.model.jpa.JpaModel;
import org.mabartos.meetmethere.model.jpa.entity.EventEntity;
import org.mabartos.meetmethere.model.jpa.util.JpaUtil;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
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
        return session.userStorage().getUserById(getEntity().getCreatorId());
    }

    @Override
    public void setCreatedBy(UserModel user) {
        getEntity().setCreatorId(user.getId());
    }

    @Override
    public String getCreatorName() {
        return getEntity().getCreatorName();
    }

    @Override
    public void setCreatorName(String creatorName) {
        getEntity().setCreatorName(creatorName);
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
        return getEntity().getOrganizersId()
                .stream()
                .map(u -> session.userStorage().getUserById(u))
                .collect(Collectors.toSet());
    }

    @Override
    public void setOrganizers(Set<UserModel> organizers) {
        final Set<String> organizersIds = organizers.stream()
                .filter(Objects::nonNull)
                .map(HasId::getId)
                .collect(Collectors.toSet());

        getEntity().setOrganizersId(organizersIds);
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
    public String getVenueName() {
        return getEntity().getVenueName();
    }

    @Override
    public void setVenueName(String venueName) {
        getEntity().setVenueName(venueName);
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
        return session.userStorage().getUserByEmail(getEntity().getUpdatedById());
    }

    @Override
    public void setUpdatedBy(UserModel user) {
        getEntity().setUpdatedById(user.getId());
    }

   /* @Override
    public Map<UserModel, ResponseType> getResponses() {
        final Map<UserModel, ResponseType> map = new HashMap<>();

        getEntity().getInvitations()
                .forEach(f -> map.put(new JpaUserAdapter(session, em, f.getReceiver()), f.getResponseType()));

        return map;
    }*/

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
