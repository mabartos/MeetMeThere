package org.mabartos.meetmethere.integration.keycloak;

import org.mabartos.meetmethere.api.provider.ProviderFactory;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;

public class KeycloakUserProviderFactory implements ProviderFactory<KeycloakUserProvider> {

    @Override
    public KeycloakUserProvider create(MeetMeThereSession session) {
        return new KeycloakUserProvider(session);
    }
}
