package org.mabartos.meetmethere.service.rest.util;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.mutiny.core.eventbus.Message;
import org.mabartos.meetmethere.api.codecs.SetHolder;
import org.mabartos.meetmethere.api.model.exception.ModelDuplicateException;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.mabartos.meetmethere.api.model.eventbus.BusExceptionUtil.isTypeOfException;

public class EventBusUtil {

    public static <T> Uni<T> createEntity(EventBus eventBus, String address, Object object) {
        return eventBus.<T>request(address, object)
                .onItem()
                .transform(Message::body)
                .onFailure(isDuplicate -> isTypeOfException(isDuplicate, ModelDuplicateException.class))
                .transform(duplicate -> new WebApplicationException(duplicate.getMessage(), 409))
                .onFailure(isOther -> !isTypeOfException(isOther, ModelDuplicateException.class)) //Workaround for failure handling
                .transform(BadRequestException::new);
    }

    public static <T> Uni<T> getSingleEntity(EventBus bus, String address, Object object) {
        return bus.<T>request(address, object)
                .onItem()
                .transform(Message::body)
                .onItem()
                .ifNull()
                .failWith(NotFoundException::new)
                .onFailure(t -> !isTypeOfException(t, WebApplicationException.class))
                .transform(t -> new BadRequestException(t.getMessage()));
    }

    public static <Original, Converted> Uni<Set<Converted>> getSetOfEntities(
            EventBus bus,
            String address,
            Object object,
            Function<Original, Converted> mappingFunction) {
        return bus.<SetHolder<Original>>request(address, object)
                .onItem()
                .transform(Message::body)
                .map(f -> f.getSet()
                        .stream()
                        .filter(Objects::nonNull)
                        .map(mappingFunction)
                        .collect(Collectors.toSet())
                );
    }
}
