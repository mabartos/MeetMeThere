package org.mabartos.meetmethere.model.jpa.util;

import javax.persistence.NoResultException;
import java.util.Optional;
import java.util.function.Supplier;

public class JpaUtil {

    public static <T> Optional<T> catchNoResult(Supplier<T> supplier) {
        return catchException(supplier, NoResultException.class);
    }

    public static <T> Optional<T> catchException(Supplier<T> supplier, Class<? extends Throwable> clazz) {
        try {
            supplier.get();
        } catch (Throwable e) {
            if (e.getClass().isAssignableFrom(clazz)) {
                return Optional.empty();
            } else throw e;
        }
        return Optional.empty();
    }

}
