package org.mabartos.meetmethere.model.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "USER")
@NoArgsConstructor
@Setter
@Getter
public class UserEntity extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    private String firstName;

    private String lastName;

    @OneToMany(mappedBy = "organizer")
    @BatchSize(size = 20)
    private Set<EventEntity> organizedEvents = new HashSet<>();

    @ElementCollection
    @BatchSize(size = 20)
    private Map<String, String> attributes = new HashMap<>();
}
