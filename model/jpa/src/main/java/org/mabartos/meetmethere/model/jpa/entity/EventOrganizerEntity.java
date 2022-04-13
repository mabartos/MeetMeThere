package org.mabartos.meetmethere.model.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Table(name = "EVENT_ORGANIZER")
@NoArgsConstructor
@Setter
@Getter
public class EventOrganizerEntity extends BaseEntity {

    @ManyToOne
    @NonNull
    private EventEntity event;

    @ManyToOne
    @NonNull
    private UserEntity organizer;
}
