package org.mabartos.meetmethere.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Setter
@Getter
public class Event {

    private Long id;

    private String title;

    private String description;

    private boolean isPublic;

    private Set<User> organizers;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private User updatedBy;

    private User creator;

    private Address venue;

    private Set<EventInvitation> invitations;

    private Map<String, String> attributes;

    public Event(String title, User creator, LocalDateTime createdAt) {
        this.title = title;
        this.creator = creator;
        this.createdAt = createdAt;
        this.organizers = new HashSet<>();
        this.attributes = new HashMap<>();
        this.invitations = new HashSet<>();
    }
}
