package org.mabartos.meetmethere.model.jpa.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.mabartos.meetmethere.model.jpa.entity.attribute.HasAttributesEntity;
import org.mabartos.meetmethere.model.jpa.entity.attribute.UserAttributeEntity;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "USER")
@NoArgsConstructor
@Setter
@Getter
public class UserEntity extends BaseEntity implements HasAttributesEntity<UserAttributeEntity> {

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    private String firstName;

    private String lastName;

    @OneToMany(mappedBy = "organizer")
    @BatchSize(size = 20)
    private Set<EventEntity> organizedEvents = new HashSet<>();

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "user")
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 20)
    protected List<UserAttributeEntity> attributes = new LinkedList<>();

    @Override
    public List<UserAttributeEntity> getAttributes() {
        return attributes;
    }
}
