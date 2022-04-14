package org.mabartos.meetmethere.mapper;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface ModelDtoMapper<DTO, Model> {
    Model toModel(DTO dto, Model model);

    DTO toDto(Model model);

    default <T> void update(Consumer<T> setter, Supplier<T> getter) {
        final T value = getter.get();
        if (value != null) {
            setter.accept(getter.get());
        }
    }
}
