package org.mabartos.meetmethere.model.jpa.entity;


import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.NoArgsConstructor;

import java.util.Objects;

@MappedSuperclass
@NoArgsConstructor
public abstract class BaseEntity extends PanacheEntity {

    @Version
    private int version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity)) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
