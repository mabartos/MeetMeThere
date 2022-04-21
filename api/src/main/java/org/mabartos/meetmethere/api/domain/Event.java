package org.mabartos.meetmethere.api.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Setter
@Getter
public class Event implements Serializable {

    private Long id;

    private String title;

    private String description;

    private boolean isPublic;

    private Set<Long> organizersId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String updatedByName;

    private String createdByName;

    private Long updatedById;

    private Long createdById;

    private Address venue;

    private Set<Long> invitationsId;

    private Map<String, String> attributes;

    public Event(String title, User createdBy) {
        this.title = title;
        this.createdByName = String.format("%s %s", createdBy.getFirstName(), createdBy.getLastName());
        this.createdById = createdBy.getId();
        this.organizersId = new HashSet<>();
        this.attributes = new HashMap<>();
        this.invitationsId = new HashSet<>();
    }
}
