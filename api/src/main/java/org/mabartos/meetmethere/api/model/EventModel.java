package org.mabartos.meetmethere.api.model;

import java.time.LocalDateTime;
import java.util.Set;

public interface EventModel extends HasId<Long>, HasAttributes<String, String> {

    UserModel getCreatedBy();

    void setCreatedBy(UserModel user);

    String getCreatorName();

    void setCreatorName(String creatorName);

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

    String getVenueName();

    void setVenueName(String venueName);

    LocalDateTime getUpdatedAt();

    void setUpdatedAt(LocalDateTime updatedAt);

    UserModel getUpdatedBy();

    void setUpdatedBy(UserModel user);

    //Map<UserModel, ResponseType> getResponses();

    Set<InvitationModel> getInvitations();
}
