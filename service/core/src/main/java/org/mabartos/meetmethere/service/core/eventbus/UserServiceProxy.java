package org.mabartos.meetmethere.service.core.eventbus;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Uni;
import org.mabartos.meetmethere.api.domain.User;
import org.mabartos.meetmethere.api.model.UserModel;
import org.mabartos.meetmethere.api.model.eventbus.PaginationObject;
import org.mabartos.meetmethere.api.model.eventbus.user.EmailUsernameObject;
import org.mabartos.meetmethere.api.model.eventbus.user.UserSet;
import org.mabartos.meetmethere.api.model.exception.ModelDuplicateException;
import org.mabartos.meetmethere.api.model.exception.ModelNotFoundException;
import org.mabartos.meetmethere.api.service.UserService;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import static org.mabartos.meetmethere.api.model.eventbus.user.UserEventsNames.USER_COUNT_EVENT;
import static org.mabartos.meetmethere.api.model.eventbus.user.UserEventsNames.USER_CREATE_BASIC_EVENT;
import static org.mabartos.meetmethere.api.model.eventbus.user.UserEventsNames.USER_CREATE_EVENT;
import static org.mabartos.meetmethere.api.model.eventbus.user.UserEventsNames.USER_GET_EMAIL_EVENT;
import static org.mabartos.meetmethere.api.model.eventbus.user.UserEventsNames.USER_GET_USERNAME_EVENT;
import static org.mabartos.meetmethere.api.model.eventbus.user.UserEventsNames.USER_GET_USERS_EVENT;
import static org.mabartos.meetmethere.api.model.eventbus.user.UserEventsNames.USER_GET_USER_EVENT;
import static org.mabartos.meetmethere.api.model.eventbus.user.UserEventsNames.USER_REMOVE_EVENT;
import static org.mabartos.meetmethere.api.model.eventbus.user.UserEventsNames.USER_UPDATE_EVENT;

@ApplicationScoped
@Transactional
public class UserServiceProxy {
    private final MeetMeThereSession session;
    private final UserService delegate;

    @Inject
    public UserServiceProxy(MeetMeThereSession session, UserService delegate) {
        this.session = session;
        this.delegate = delegate;
    }

    @ConsumeEvent(value = USER_GET_USER_EVENT, blocking = true)
    public Uni<User> getUserById(String id) throws ModelNotFoundException {
        return delegate.getUserById(id);
    }

    @ConsumeEvent(value = USER_GET_USERNAME_EVENT, blocking = true)
    public Uni<User> getUserByUsername(String username) throws ModelNotFoundException {
        return delegate.getUserByUsername(username);
    }

    @ConsumeEvent(value = USER_GET_EMAIL_EVENT, blocking = true)
    public Uni<User> getUserByEmail(String email) throws ModelNotFoundException {
        return delegate.getUserByEmail(email);
    }

    @ConsumeEvent(value = USER_GET_USERS_EVENT, blocking = true)
    public Uni<UserSet> getUsers(PaginationObject paginationObject) {
        return delegate.getUsers(paginationObject.getFirstResult(), paginationObject.getMaxResult())
                .onItem()
                .transform(UserSet::new);
    }

    @ConsumeEvent(value = USER_COUNT_EVENT, blocking = true)
    public Uni<Long> getUsersCount(Object ignore) {
        return delegate.getUsersCount();
    }

    @ConsumeEvent(value = USER_CREATE_BASIC_EVENT, blocking = true)
    public Uni<String> createUser(EmailUsernameObject object) throws ModelDuplicateException {
        return delegate.createUser(object.getEmail(), object.getUsername());
    }

    @ConsumeEvent(value = USER_CREATE_EVENT, blocking = true)
    public Uni<String> createUser(User user) throws ModelDuplicateException {
        return delegate.createUser(user);
    }

    @ConsumeEvent(value = USER_REMOVE_EVENT, blocking = true)
    public void removeUser(String id) throws ModelNotFoundException {
        delegate.removeUser(id);
    }

    @ConsumeEvent(value = USER_UPDATE_EVENT, blocking = true)
    public Uni<UserModel> updateUser(User user) throws ModelNotFoundException {
        return delegate.updateUser(user);
    }
}
