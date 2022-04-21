package org.mabartos.meetmethere.api.model.eventbus;

import org.mabartos.meetmethere.api.codecs.SetHolder;
import org.mabartos.meetmethere.api.model.UserModel;

import java.util.Set;

public class UserModelSet extends SetHolder<UserModel> {

    public UserModelSet(final UserModel... events) {
        super(events);
    }

    public UserModelSet(final Set<UserModel> set) {
        super(set);
    }
}
