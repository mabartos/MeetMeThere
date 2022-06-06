package org.mabartos.meetmethere.model.jpa.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "EVENT")
@NoArgsConstructor
@Setter
@Getter
public class EventEntity extends BaseEntity {

    @Column(nullable = false)
    private String title;

    private String description;

    private boolean isPublic = false;

    @ElementCollection(fetch = FetchType.EAGER)
    @BatchSize(size = 20)
    private Set<String> organizersId = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String updatedById;

    private String creatorId;

    private String creatorName;

    @Embedded
    private AddressEntity venue;

    private String venueName;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "event")
    @BatchSize(size = 20)
    private Set<InvitationEntity> invitations;

    @ElementCollection(fetch = FetchType.EAGER)
    @BatchSize(size = 20)
    private Map<String, String> attributes = new HashMap<>();
}
