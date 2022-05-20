package org.mabartos.meetmethere.service.core.store;

import org.mabartos.meetmethere.api.provider.InvitationProvider;
import org.mabartos.meetmethere.api.provider.ProviderFactory;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;
import org.mabartos.meetmethere.model.caffeine.provider.CaffeineEventInvitationProvider;
import org.mabartos.meetmethere.model.jpa.provider.JpaEventInvitationProviderFactory;

public class InvitationStoreFactory implements ProviderFactory<InvitationProvider> {

    @Override
    public InvitationProvider create(MeetMeThereSession session) {
        return new CaffeineEventInvitationProvider(session, new JpaEventInvitationProviderFactory().create(session));
    }
}
