package org.mabartos.meetmethere.service.core.session;

import org.mabartos.meetmethere.api.session.AppContext;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;

public class DefaultAppContext implements AppContext {
    private final MeetMeThereSession session;
    private Long currentEventId;
    private Long currentInvitationId;

    public DefaultAppContext(MeetMeThereSession session) {
        this.session = session;
    }

    @Override
    public Long getCurrentEventId() {
        return currentEventId;
    }

    @Override
    public void setCurrentEvent(Long id) {
        this.currentEventId = id;
    }

    @Override
    public Long getCurrentInvitation() {
        return currentInvitationId;
    }

    @Override
    public void setCurrentInvitation(Long id) {
        this.currentInvitationId = id;
    }
}
