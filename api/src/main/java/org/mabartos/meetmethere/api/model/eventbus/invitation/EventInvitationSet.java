package org.mabartos.meetmethere.api.model.eventbus.invitation;

import org.mabartos.meetmethere.api.codecs.SetHolder;
import org.mabartos.meetmethere.api.domain.EventInvitation;

import java.util.Set;

public class EventInvitationSet extends SetHolder<EventInvitation> {

    public EventInvitationSet(final EventInvitation... events) {
        super(events);
    }

    public EventInvitationSet(final Set<EventInvitation> set) {
        super(set);
    }

}
