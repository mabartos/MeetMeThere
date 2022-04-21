package org.mabartos.meetmethere.api.model.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mabartos.meetmethere.api.enums.ResponseType;

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

    private String message;
}
