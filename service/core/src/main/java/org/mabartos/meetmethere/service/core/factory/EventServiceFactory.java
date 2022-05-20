package org.mabartos.meetmethere.service.core.factory;

import org.mabartos.meetmethere.api.provider.ProviderFactory;
import org.mabartos.meetmethere.api.service.EventService;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;
import org.mabartos.meetmethere.service.core.DefaultEventService;

public class EventServiceFactory implements ProviderFactory<EventService> {

    @Override
    public EventService create(MeetMeThereSession session) {
        return new DefaultEventService(session);
    }
}
