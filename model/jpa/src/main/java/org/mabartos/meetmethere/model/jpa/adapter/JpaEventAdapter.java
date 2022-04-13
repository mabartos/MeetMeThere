package org.mabartos.meetmethere.model.jpa.adapter;

import jakarta.persistence.EntityManager;
import org.mabartos.meetmethere.enums.ResponseType;
import org.mabartos.meetmethere.model.AddressModel;
import org.mabartos.meetmethere.model.EventModel;
import org.mabartos.meetmethere.model.InvitationModel;
import org.mabartos.meetmethere.model.UserModel;
import org.mabartos.meetmethere.model.jpa.JpaModel;
import org.mabartos.meetmethere.model.jpa.entity.EventEntity;
import org.mabartos.meetmethere.model.jpa.entity.EventOrganizerEntity;
import org.mabartos.meetmethere.model.jpa.entity.attribute.EventAttributeEntity;
import org.mabartos.meetmethere.session.MeetMeThereSession;

import java.time.LocalDateTime;
import java.util.Map;
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
        return entity.id;
    }

    @Override
    public void setId(Long id) {
        entity.id = id;
    }

    @Override
    public String getEventTitle() {
        return entity.getTitle();
    }

    @Override
    public void setEventTitle(String title) {
        entity.setTitle(title);
    }

    @Override
    public String getDescription() {
        return entity.getDescription();
    }

    @Override
    public void setDescription(String description) {
        entity.setDescription(description);
    }

    @Override
    public UserModel getCreatedBy() {
        return new JpaUserAdapter(session, em, entity.getCreator());
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return entity.getCreatedAt();
    }

    @Override
    public boolean isPublic() {
        return entity.isPublic();
    }

    @Override
    public void setPublic(boolean isPublic) {
        entity.setPublic(isPublic);
    }

    @Override
    public Set<UserModel> getOrganizers() {
        return entity.getOrganizers()
                .stream()
                .map(EventOrganizerEntity::getOrganizer)
                .map(u -> new JpaUserAdapter(session, em, u))
                .collect(Collectors.toSet());
    }

    @Override
    public void setOrganizers(Set<UserModel> organizers) {
        // TODO
        // session.users().organize()...
    }

    @Override
    public LocalDateTime getStartTime() {
        return entity.getStartTime();
    }

    @Override
    public void setStartTime(LocalDateTime startTime) {
        entity.setStartTime(startTime);
    }

    @Override
    public LocalDateTime getEndTime() {
        return entity.getEndTime();
    }

    @Override
    public void setEndTime(LocalDateTime endTime) {
        entity.setEndTime(endTime);
    }

    @Override
    public AddressModel getVenue() {
        return new JpaAddressAdapter(session, em, entity.getVenue());
    }

    @Override
    public void setVenue(AddressModel venue) {
        //TODO
    }

    @Override
    public LocalDateTime getUpdatedAt() {
        return entity.getUpdatedAt();
    }

    @Override
    public void setUpdatedAt(LocalDateTime updatedAt) {
        entity.setUpdatedAt(updatedAt);
    }

    @Override
    public UserModel getUpdatedBy() {
        return new JpaUserAdapter(session, em, entity.getUpdatedBy());
    }

    @Override
    public Map<UserModel, ResponseType> getResponses() {
        //TODO
        return null;
    }

    @Override
    public Set<InvitationModel> getInvitations() {
        return entity.getInvitations()
                .stream()
                .map(f -> new JpaInvitationAdapter(session, em, f))
                .collect(Collectors.toSet());
    }

    @Override
    public EventEntity getEntity() {
        return entity;
    }
}
