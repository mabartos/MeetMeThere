package org.mabartos.meetmethere.api.model.eventbus;

public class BusExceptionUtil {

    public static boolean isTypeOfException(Throwable throwable, Class<? extends Throwable> type) {
        if (throwable == null) return false;
        if (throwable.getClass().isAssignableFrom(type) || throwable.getMessage().contains(type.getName())) return true;

        if (throwable.getCause() == null) return false;
        return throwable.getCause().getClass().isAssignableFrom(type) || throwable.getCause().getMessage().contains(type.getName());
    }
}
