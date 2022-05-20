package org.mabartos.meetmethere.service.core.store;

import org.mabartos.meetmethere.api.provider.EventProvider;
import org.mabartos.meetmethere.api.provider.ProviderFactory;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;
import org.mabartos.meetmethere.model.caffeine.provider.CaffeineEventProvider;
import org.mabartos.meetmethere.model.jpa.provider.JpaEventProviderFactory;

public class EventStoreFactory implements ProviderFactory<EventProvider> {

    @Override
    public EventProvider create(MeetMeThereSession session) {
        return new CaffeineEventProvider(session, new JpaEventProviderFactory().create(session));
    }
}
