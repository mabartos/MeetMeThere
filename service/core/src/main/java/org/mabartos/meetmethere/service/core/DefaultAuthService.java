package org.mabartos.meetmethere.service.core;

import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.mabartos.meetmethere.api.service.AuthService;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

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
    public boolean isAuthenticated() {
        return jwt != null && jwt.getClaimNames() != null && jwt.getSubject() != null;
    }

    @Override
    public boolean isMyId(String id) {
        return jwt != null && sub.equals(id);
    }
}
