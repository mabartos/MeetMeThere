package org.mabartos.meetmethere.api.model.eventbus.event;

import org.mabartos.meetmethere.api.codecs.SetHolder;
import org.mabartos.meetmethere.api.domain.Event;

import java.util.Set;

public class EventSet extends SetHolder<Event> {

    public EventSet(final Event... events) {
        super(events);
    }

    public EventSet(final Set<Event> set) {
        super(set);
    }
}
