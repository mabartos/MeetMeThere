package org.mabartos.meetmethere.api.model.jpa.provider;

import org.mabartos.meetmethere.api.provider.InvitationProvider;
import org.mabartos.meetmethere.api.provider.ProviderFactory;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;

public class JpaEventInvitationProviderFactory implements ProviderFactory<InvitationProvider> {
    @Override
    public InvitationProvider create(MeetMeThereSession session) {
        return new JpaEventInvitationProvider(session, session.entityManager());
    }
}
