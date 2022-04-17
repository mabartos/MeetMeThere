package org.mabartos.meetmethere.model.jpa.util;

import org.mabartos.meetmethere.model.HasId;
import org.mabartos.meetmethere.model.jpa.JpaModel;
import org.mabartos.meetmethere.model.jpa.entity.BaseEntity;

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
            if (e.getClass().isAssignableFrom(clazz)) {
                return Optional.empty();
            } else throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends JpaModel<?>, U extends HasId<?>, V extends BaseEntity>
    V convertToEntity(U model, EntityManager em, Class<T> adapterClass, Class<V> entityClass) {
        if (model == null || adapterClass == null || entityClass == null) return null;

        if (model.getClass().isAssignableFrom(adapterClass)) {
            return (V) adapterClass.cast(model).getEntity();
        } else {
            return catchNoResult(() -> em.find(entityClass, model.getId())).orElse(null);
        }
    }

}
