package org.mabartos.meetmethere.model;

import org.mabartos.meetmethere.enums.ResponseType;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public interface EventModel extends HasId<Long>, HasAttributes<String, String> {

    UserModel getCreatedBy();

    LocalDateTime getCreatedAt();

    boolean isPublic();

    void setPublic(boolean isPublic);

    Set<UserModel> getOrganizers();

    void setOrganizers(Set<UserModel> organizers);

    String getEventTitle();

    void setEventTitle(String title);

    String getDescription();

    void setDescription(String description);

    LocalDateTime getStartTime();

    void setStartTime(LocalDateTime startTime);

    LocalDateTime getEndTime();

    void setEndTime(LocalDateTime endTime);

    AddressModel getVenue();

    void setVenue(AddressModel venue);

    LocalDateTime getUpdatedAt();

    void setUpdatedAt(LocalDateTime updatedAt);

    UserModel getUpdatedBy();

    Map<UserModel, ResponseType> getResponses();

    Set<InvitationModel> getInvitations();
}
