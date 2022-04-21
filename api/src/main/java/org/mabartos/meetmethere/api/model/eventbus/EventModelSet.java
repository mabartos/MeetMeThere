package org.mabartos.meetmethere.api.model.eventbus;

import org.mabartos.meetmethere.api.codecs.SetHolder;
import org.mabartos.meetmethere.api.model.EventModel;

import java.util.Set;

public class EventModelSet extends SetHolder<EventModel> {

    public EventModelSet(final EventModel... events) {
        super(events);
    }

    public EventModelSet(final Set<EventModel> set) {
        super(set);
    }
}
