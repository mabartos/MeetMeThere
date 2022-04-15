package org.mabartos.meetmethere;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class UpdateUtil {

    public static <T> void update(Consumer<T> setter, Supplier<T> getter) {
        final T value = getter.get();
        if (value != null) {
            setter.accept(getter.get());
        }
    }

}
