package org.mabartos.meetmethere.service.core;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Uni;
import org.mabartos.meetmethere.api.domain.User;
import org.mabartos.meetmethere.api.mapper.ModelToDomain;
import org.mabartos.meetmethere.api.model.ModelUpdater;
import org.mabartos.meetmethere.api.model.UserModel;
import org.mabartos.meetmethere.api.model.eventbus.EmailUsernameObject;
import org.mabartos.meetmethere.api.model.eventbus.PaginationObject;
import org.mabartos.meetmethere.api.model.eventbus.UserSet;
import org.mabartos.meetmethere.api.model.exception.ModelDuplicateException;
import org.mabartos.meetmethere.api.model.exception.ModelNotFoundException;
import org.mabartos.meetmethere.api.service.UserService;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
    public Uni<User> getUserById(Long id) {
        final UserModel model = session.userStorage().getUserById(id);
        if (model == null) return null;

        return Uni.createFrom()
                .item(session.userStorage().getUserById(id))
                .onItem()
                .transform(ModelToDomain::toDomain);
    }

    @Override
    @ConsumeEvent(value = USER_GET_USERNAME_EVENT, blocking = true)
    public Uni<User> getUserByUsername(String username) {
        return Uni.createFrom()
                .item(session.userStorage().getUserByUsername(username))
                .onItem()
                .transform(ModelToDomain::toDomain);
    }

    @Override
    @ConsumeEvent(value = USER_GET_EMAIL_EVENT, blocking = true)
    public Uni<User> getUserByEmail(String email) {
        return Uni.createFrom()
                .item(session.userStorage().getUserByEmail(email))
                .onItem()
                .transform(ModelToDomain::toDomain);
    }

    @Override
    @ConsumeEvent(value = USER_GET_USERS_EVENT, blocking = true)
    public Uni<UserSet> getUsers(PaginationObject paginationObject) {
        final Set<User> userSet = session.userStorage()
                .getUsers(paginationObject.getFirstResult(), paginationObject.getMaxResult())
                .stream()
                .map(ModelToDomain::toDomain)
                .collect(Collectors.toSet());
        return Uni.createFrom().item(new UserSet(userSet));
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
