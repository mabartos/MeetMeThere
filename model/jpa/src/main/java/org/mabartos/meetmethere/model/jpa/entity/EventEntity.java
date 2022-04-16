package org.mabartos.meetmethere.model.jpa.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    @ManyToMany
    @JoinTable(
            name = "Event_Organizers",
            joinColumns = {@JoinColumn(name = "event_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    @BatchSize(size = 20)
    private Set<UserEntity> organizers = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = UserEntity.class)
    private UserEntity updatedBy;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = UserEntity.class)
    private UserEntity creator;

    @Embedded
    private AddressEntity venue;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "event")
    @BatchSize(size = 20)
    private Set<InvitationEntity> invitations;

    @ElementCollection
    @BatchSize(size = 20)
    private Map<String, String> attributes = new HashMap<>();
}
