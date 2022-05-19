package org.mabartos.meetmethere.model.jpa.provider;

import org.mabartos.meetmethere.api.provider.EventProvider;
import org.mabartos.meetmethere.api.provider.ProviderFactory;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;

public class JpaEventProviderFactory implements ProviderFactory<EventProvider> {

    @Override
    public EventProvider create(MeetMeThereSession session) {
        return new JpaEventProvider(session, session.entityManager());
    }
}
