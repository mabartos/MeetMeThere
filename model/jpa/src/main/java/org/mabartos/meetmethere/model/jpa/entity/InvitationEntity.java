package org.mabartos.meetmethere.model.jpa.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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

    private String message;
}
