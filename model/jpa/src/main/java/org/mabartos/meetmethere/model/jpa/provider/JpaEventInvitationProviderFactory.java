package org.mabartos.meetmethere.model.jpa.provider;

import org.mabartos.meetmethere.model.provider.InvitationProvider;
import org.mabartos.meetmethere.model.provider.ProviderFactory;
import org.mabartos.meetmethere.session.MeetMeThereSession;

public class JpaEventInvitationProviderFactory implements ProviderFactory<InvitationProvider> {
    @Override
    public InvitationProvider create(MeetMeThereSession session) {
        return new JpaEventInvitationProvider(session, session.entityManager());
    }
}
