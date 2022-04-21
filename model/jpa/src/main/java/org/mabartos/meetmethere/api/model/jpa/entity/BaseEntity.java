package org.mabartos.meetmethere.api.model.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@MappedSuperclass
@NoArgsConstructor
public class BaseEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue
    @Getter
    @Setter
    private Long id;

    @Version
    private int version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity)) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
