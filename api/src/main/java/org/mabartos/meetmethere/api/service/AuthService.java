package org.mabartos.meetmethere.api.service;

import io.quarkus.security.ForbiddenException;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.mabartos.meetmethere.api.authz.PermissionEvaluator;
import org.mabartos.meetmethere.api.domain.Event;
import org.mabartos.meetmethere.api.model.UserModel;

public interface AuthService {

    JsonWebToken getToken();

    UserModel getAuthenticatedUser();

    boolean isAuthenticated();

    boolean isAdmin();

    boolean isMyId(String id);

    default void assertMyId(String id) {
        if (!isMyId(id)) throw new ForbiddenException("You are not eligible to access this resource");
    }

    PermissionEvaluator<Event, Long> events();
}
