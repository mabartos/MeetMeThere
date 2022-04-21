package org.mabartos.meetmethere.api;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class UpdateUtil {

    public static <T> void update(Consumer<T> setter, Supplier<T> getter) {
        try {
            Optional.ofNullable(getter)
                    .map(Supplier::get)
                    .ifPresent(setter);
        } catch (NullPointerException ignore) {
            //NOP
        }
    }

}
