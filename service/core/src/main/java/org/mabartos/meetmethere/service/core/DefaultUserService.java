package org.mabartos.meetmethere.service.core;

import io.smallrye.mutiny.Uni;
import org.mabartos.meetmethere.api.domain.User;
import org.mabartos.meetmethere.api.mapper.ModelToDomain;
import org.mabartos.meetmethere.api.model.ModelUpdater;
import org.mabartos.meetmethere.api.model.UserModel;
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
    public Uni<User> getUserById(Long id) throws ModelNotFoundException {
        final UserModel model = session.userStorage().getUserById(id);
        if (model == null) throw new ModelNotFoundException();

        return Uni.createFrom()
                .item(model)
                .onItem()
                .transform(ModelToDomain::toDomain);
    }

    @Override
    public Uni<User> getUserByUsername(String username) throws ModelNotFoundException {
        final UserModel model = session.userStorage().getUserByUsername(username);
        if (model == null) throw new ModelNotFoundException();

        return Uni.createFrom()
                .item(model)
                .onItem()
                .transform(ModelToDomain::toDomain);
    }

    @Override
    public Uni<User> getUserByEmail(String email) throws ModelNotFoundException {
        final UserModel model = session.userStorage().getUserByEmail(email);
        if (model == null) throw new ModelNotFoundException();

        return Uni.createFrom()
                .item(model)
                .onItem()
                .transform(ModelToDomain::toDomain);
    }

    @Override
    public Uni<Set<User>> getUsers(Integer firstResult, Integer maxResults) {
        final Set<User> userSet = session.userStorage()
                .getUsers(firstResult, maxResults)
                .stream()
                .map(ModelToDomain::toDomain)
                .collect(Collectors.toSet());
        return Uni.createFrom().item(userSet);
    }

    @Override
    public Uni<Long> getUsersCount() {
        return Uni.createFrom().item(session.userStorage().getUsersCount());
    }

    @Override
    public Uni<Long> createUser(String email, String username) throws ModelDuplicateException {
        return Uni.createFrom().item(session.userStorage().createUser(email, username).getId());
    }

    @Override
    public Uni<Long> createUser(User user) throws ModelDuplicateException {
        UserModel model = session.userStorage().createUser(user.getEmail(), user.getUsername());
        ModelUpdater.updateModel(user, model);

        final Long id = session.userStorage().updateUser(model).getId();
        return Uni.createFrom().item(id);
    }

    @Override
    public void removeUser(Long id) {
        session.userStorage().removeUser(id);
    }

    @Override
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
