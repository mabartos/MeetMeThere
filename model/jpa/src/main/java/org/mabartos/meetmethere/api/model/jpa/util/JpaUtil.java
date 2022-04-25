package org.mabartos.meetmethere.api.model.jpa.util;

import org.mabartos.meetmethere.api.model.HasId;
import org.mabartos.meetmethere.api.model.jpa.JpaModel;
import org.mabartos.meetmethere.api.model.jpa.entity.BaseEntity;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Optional;
import java.util.function.Supplier;

public class JpaUtil {

    public static <T> Optional<T> catchNoResult(Supplier<T> supplier) {
        return catchException(supplier, NoResultException.class);
    }

    public static <T> Optional<T> catchException(Supplier<T> supplier, Class<? extends Throwable> clazz) {
        try {
            return Optional.of(supplier.get());
        } catch (Throwable e) {
            if (clazz.isAssignableFrom(e.getClass())) {
                return Optional.empty();
            } else throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends JpaModel<?>, U extends HasId<?>, V extends BaseEntity>
    V convertToEntity(U model, EntityManager em, Class<T> adapterClass, Class<V> entityClass) {
        if (model == null || adapterClass == null || entityClass == null) return null;

        if (adapterClass.isAssignableFrom(model.getClass())) {
            return (V) adapterClass.cast(model).getEntity();
        } else {
            return catchNoResult(() -> em.find(entityClass, model.getId())).orElse(null);
        }
    }

}
