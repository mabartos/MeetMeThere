package org.mabartos.meetmethere.api.model.eventbus.user;

import org.mabartos.meetmethere.api.codecs.SetHolder;
import org.mabartos.meetmethere.api.domain.User;

import java.util.Set;

public class UserSet extends SetHolder<User> {

    public UserSet(final User... events) {
        super(events);
    }

    public UserSet(final Set<User> set) {
        super(set);
    }
}
