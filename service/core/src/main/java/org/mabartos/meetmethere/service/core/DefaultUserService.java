package org.mabartos.meetmethere.service.core;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Uni;
import org.mabartos.meetmethere.api.domain.User;
import org.mabartos.meetmethere.api.model.ModelUpdater;
import org.mabartos.meetmethere.api.model.UserModel;
import org.mabartos.meetmethere.api.model.eventbus.EmailUsernameObject;
import org.mabartos.meetmethere.api.model.eventbus.PaginationObject;
import org.mabartos.meetmethere.api.model.eventbus.UserModelSet;
import org.mabartos.meetmethere.api.model.exception.ModelDuplicateException;
import org.mabartos.meetmethere.api.model.exception.ModelNotFoundException;
import org.mabartos.meetmethere.api.service.UserService;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class DefaultUserService implements UserService {
    private final MeetMeThereSession session;

    @Inject
    public DefaultUserService(MeetMeThereSession session) {
        this.session = session;
    }

    @Override
    @ConsumeEvent(value = USER_GET_USER_EVENT, blocking = true)
    public Uni<UserModel> getUserById(Long id) {
        return Uni.createFrom().item(session.userStorage().getUserById(id));
    }

    @Override
    @ConsumeEvent(value = USER_GET_USERNAME_EVENT, blocking = true)
    public Uni<UserModel> getUserByUsername(String username) {
        return Uni.createFrom().item(session.userStorage().getUserByUsername(username));
    }

    @Override
    @ConsumeEvent(value = USER_GET_EMAIL_EVENT, blocking = true)
    public Uni<UserModel> getUserByEmail(String email) {
        return Uni.createFrom().item(session.userStorage().getUserByEmail(email));
    }

    @Override
    @ConsumeEvent(value = USER_GET_USERS_EVENT, blocking = true)
    public Uni<UserModelSet> getUsers(PaginationObject paginationObject) {
        return Uni.createFrom()
                .item(new UserModelSet(session.userStorage().getUsers(paginationObject.getFirstResult(), paginationObject.getMaxResult())));
    }

    @Override
    @ConsumeEvent(value = USER_COUNT_EVENT, blocking = true)
    public Uni<Long> getUsersCount(Object ignore) {
        return Uni.createFrom().item(session.userStorage().getUsersCount());
    }

    @Override
    @ConsumeEvent(value = USER_CREATE_BASIC_EVENT, blocking = true)
    public Uni<Long> createUser(EmailUsernameObject object) throws ModelDuplicateException {
        return Uni.createFrom().item(session.userStorage().createUser(object.getEmail(), object.getUsername()).getId());
    }

    @Override
    @ConsumeEvent(value = USER_CREATE_EVENT, blocking = true)
    public Uni<Long> createUser(User user) throws ModelDuplicateException {
        UserModel model = session.userStorage().createUser(user.getEmail(), user.getUsername());
        ModelUpdater.updateModel(user, model);

        final Long id = session.userStorage().updateUser(model).getId();
        return Uni.createFrom().item(id);
    }

    @Override
    @ConsumeEvent(value = USER_REMOVE_EVENT, blocking = true)
    public void removeUser(Long id) throws ModelNotFoundException {
        session.userStorage().removeUser(id);
    }

    @Override
    @ConsumeEvent(value = USER_UPDATE_EVENT, blocking = true)
    public Uni<UserModel> updateUser(User user) throws ModelNotFoundException {
        UserModel model = Optional.ofNullable(session.userStorage().getUserById(user.getId()))
                .orElseThrow(ModelNotFoundException::new);

        try {
            ModelUpdater.updateModel(user, model);
            return Uni.createFrom().item(session.userStorage().updateUser(model));
        } catch (IllegalArgumentException e) {
            throw new ModelNotFoundException();
        }
    }
}
