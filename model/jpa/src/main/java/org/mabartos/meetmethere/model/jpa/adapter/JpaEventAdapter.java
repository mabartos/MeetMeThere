package org.mabartos.meetmethere.model.jpa.adapter;

import jakarta.persistence.EntityManager;
import org.mabartos.meetmethere.enums.ResponseType;
import org.mabartos.meetmethere.model.AddressModel;
import org.mabartos.meetmethere.model.EventModel;
import org.mabartos.meetmethere.model.InvitationModel;
import org.mabartos.meetmethere.model.UserModel;
import org.mabartos.meetmethere.model.jpa.JpaModel;
import org.mabartos.meetmethere.model.jpa.entity.AddressEntity;
import org.mabartos.meetmethere.model.jpa.entity.EventEntity;
import org.mabartos.meetmethere.model.jpa.entity.UserEntity;
import org.mabartos.meetmethere.model.jpa.entity.attribute.EventAttributeEntity;
import org.mabartos.meetmethere.session.MeetMeThereSession;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class JpaEventAdapter extends JpaAttributesAdapter<EventAttributeEntity> implements EventModel, JpaModel<EventEntity> {
    private final EventEntity entity;

    public JpaEventAdapter(MeetMeThereSession session, EntityManager em, EventEntity entity) {
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
                .map(f -> UserEntity.findByIdOptional(f.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(f -> (UserEntity) f)
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
        return new JpaAddressAdapter(session, em, getEntity().getVenue());
    }

    @Override
    public void setVenue(AddressModel venue) {
        getEntity().setVenue(AddressEntity.findById(venue.getId()));
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
}
