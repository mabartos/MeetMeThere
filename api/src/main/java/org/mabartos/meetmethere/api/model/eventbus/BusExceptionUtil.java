package org.mabartos.meetmethere.api.model.eventbus;

public class BusExceptionUtil {

    public static boolean isTypeOfException(Throwable throwable, Class<? extends Throwable> type) {
        if (throwable == null) return false;
        if (type.isAssignableFrom(throwable.getClass()) || throwable.getMessage().contains(type.getName())) return true;

        if (throwable.getCause() == null) return false;
        return type.isAssignableFrom(throwable.getCause().getClass()) || throwable.getCause().getMessage().contains(type.getName());
    }
}
