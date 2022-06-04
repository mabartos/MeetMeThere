package org.mabartos.meetmethere.api.service;

import io.quarkus.security.ForbiddenException;
import org.eclipse.microprofile.jwt.JsonWebToken;

public interface AuthService {

    JsonWebToken getToken();

    boolean isAuthenticated();

    boolean isMyId(String id);

    default void assertMyId(String id) {
        if (!isMyId(id)) throw new ForbiddenException("You are not eligible to access this resource");
    }
}
