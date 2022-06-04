package org.mabartos.meetmethere.api.authz;

import io.quarkus.security.ForbiddenException;

public interface PermissionEvaluator<T, ID> {
    String ADMIN_ROLE = "ADMIN";

    boolean canView();

    boolean canView(T object);

    boolean canViewId(ID id);

    default void requireView() {
        if (!canView()) throw new ForbiddenException("Cannot view a particular object");
    }

    default void requireView(T object) {
        if (!canView(object)) throw new ForbiddenException("Cannot view a particular object");
    }

    default void requireViewId(ID id) {
        if (!canViewId(id)) throw new ForbiddenException("Cannot view a particular object");
    }

    boolean canManage(T object);

    boolean canManageId(ID id);

    default void requireManage(T object) {
        if (canManage(object)) throw new ForbiddenException("Cannot view a particular object");
    }

    default void requireManageId(ID id) {
        if (canManageId(id)) throw new ForbiddenException("Cannot view a particular object");
    }
}
