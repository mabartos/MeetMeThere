package org.mabartos.meetmethere.model.jpa.entity.attribute;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import org.mabartos.meetmethere.model.jpa.entity.BaseEntity;
import org.mabartos.meetmethere.model.jpa.entity.EventEntity;

@Entity
@Table(name = "EVENT_ATTRIBUTE")
@NoArgsConstructor
@Setter
@Getter
public class EventAttributeEntity extends BaseEntity implements AttributeContent {

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = EventEntity.class)
    @JoinColumn(name = "attributes")
    private EventEntity event;

    private String key;

    @Nationalized
    private String value;

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }
}
