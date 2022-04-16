package org.mabartos.meetmethere.model.jpa.provider;

import org.mabartos.meetmethere.model.provider.EventProvider;
import org.mabartos.meetmethere.model.provider.ProviderFactory;
import org.mabartos.meetmethere.session.MeetMeThereSession;

public class JpaEventProviderFactory implements ProviderFactory<EventProvider> {

    @Override
    public EventProvider create(MeetMeThereSession session) {
        return new JpaEventProvider(session, session.entityManager());
    }
}
