package org.mabartos.meetmethere.model.jpa.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;
import org.mabartos.meetmethere.model.jpa.entity.attribute.EventAttributeEntity;
import org.mabartos.meetmethere.model.jpa.entity.attribute.HasAttributesEntity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "EVENT")
@NoArgsConstructor
@Setter
@Getter
public class EventEntity extends BaseEntity implements HasAttributesEntity<EventAttributeEntity> {

    private String title;

    private String description;

    private boolean isPublic = false;

    @ManyToMany(mappedBy = "event")
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

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = AddressEntity.class)
    private AddressEntity venue;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "event")
    @BatchSize(size = 20)
    private Set<InvitationEntity> invitations;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "event")
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 20)
    protected List<EventAttributeEntity> attributes = new LinkedList<>();

    @Override
    public List<EventAttributeEntity> getAttributes() {
        return attributes;
    }
}
