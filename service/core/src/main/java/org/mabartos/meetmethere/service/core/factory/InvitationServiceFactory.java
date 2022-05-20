package org.mabartos.meetmethere.service.core.factory;

import org.mabartos.meetmethere.api.provider.ProviderFactory;
import org.mabartos.meetmethere.api.service.EventInvitationService;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;

public class InvitationServiceFactory implements ProviderFactory<EventInvitationService> {
    @Override
    public EventInvitationService create(MeetMeThereSession session) {
        return null;
    }
}
