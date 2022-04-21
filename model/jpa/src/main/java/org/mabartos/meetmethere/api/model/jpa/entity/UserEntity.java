package org.mabartos.meetmethere.api.model.jpa.entity;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
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

    @ManyToMany(mappedBy = "organizers")
    @BatchSize(size = 20)
    private Set<EventEntity> organizedEvents = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @BatchSize(size = 20)
    private Map<String, String> attributes = new HashMap<>();
}
