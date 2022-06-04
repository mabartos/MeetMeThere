package org.mabartos.meetmethere.service.core;

import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.mabartos.meetmethere.api.authz.EventPermissionEvaluator;
import org.mabartos.meetmethere.api.model.UserModel;
import org.mabartos.meetmethere.api.service.AuthService;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;
import org.mabartos.meetmethere.service.core.authz.DefaultEventPermissionEvaluator;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import static org.mabartos.meetmethere.api.authz.PermissionEvaluator.ADMIN_ROLE;

@ApplicationScoped
public class DefaultAuthService implements AuthService {
    private final MeetMeThereSession session;

    @Inject
    JsonWebToken jwt;

    @Inject
    @Claim(standard = Claims.sub)
    String sub;

    @Inject
    public DefaultAuthService(MeetMeThereSession session) {
        this.session = session;
    }

    @Override
    public JsonWebToken getToken() {
        return jwt;
    }

    @Override
    public UserModel getAuthenticatedUser() {
        return session.userStorage().getUserById(sub);
    }

    @Override
    public boolean isAuthenticated() {
        return jwt != null && jwt.getClaimNames() != null && jwt.getSubject() != null;
    }

    @Override
    public boolean isAdmin() {
        return isAuthenticated() && getAuthenticatedUser().getRoles().contains(ADMIN_ROLE);
    }

    @Override
    public boolean isMyId(String id) {
        return jwt != null && sub.equals(id);
    }

    @Override
    public EventPermissionEvaluator events() {
        return new DefaultEventPermissionEvaluator(session);
    }
}
