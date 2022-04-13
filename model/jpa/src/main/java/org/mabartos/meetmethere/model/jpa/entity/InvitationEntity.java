package org.mabartos.meetmethere.model.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mabartos.meetmethere.enums.ResponseType;

@Entity
@Table(name = "INVITATION")
@NoArgsConstructor
@Setter
@Getter
public class InvitationEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = EventEntity.class)
    private EventEntity event;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = UserEntity.class)
    private UserEntity sender;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = UserEntity.class)
    private UserEntity receiver;

    @Enumerated
    private ResponseType responseType;
}
