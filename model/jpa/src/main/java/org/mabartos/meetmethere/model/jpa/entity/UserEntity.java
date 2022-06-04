package org.mabartos.meetmethere.model.jpa.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "USER")
@NoArgsConstructor
@Setter
@Getter
@Deprecated
public class UserEntity extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    private String firstName;

    private String lastName;

    @ElementCollection(fetch = FetchType.EAGER)
    @BatchSize(size = 20)
    private Set<Long> organizedEvents = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @BatchSize(size = 20)
    private Map<String, String> attributes = new HashMap<>();
}
