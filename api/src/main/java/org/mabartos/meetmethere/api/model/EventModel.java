package org.mabartos.meetmethere.api.model;

import org.mabartos.meetmethere.api.enums.ResponseType;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public interface EventModel extends HasId<Long>, HasAttributes<String, String> {

    UserModel getCreatedBy();

    void setCreatedBy(UserModel user);

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

    void setUpdatedBy(UserModel user);

    Map<UserModel, ResponseType> getResponses();

    Set<InvitationModel> getInvitations();
}
