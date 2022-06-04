package org.mabartos.meetmethere.api.service;

import io.smallrye.mutiny.Uni;
import org.mabartos.meetmethere.api.domain.User;
import org.mabartos.meetmethere.api.model.UserModel;
import org.mabartos.meetmethere.api.model.exception.ModelDuplicateException;
import org.mabartos.meetmethere.api.model.exception.ModelNotFoundException;

import java.util.Set;

/**
 * We should keep the responsibility up to Identity providers to manage users
 */
public interface UserService {
    Uni<User> getUserById(String id) throws ModelNotFoundException;

    Uni<User> getUserByUsername(String username) throws ModelNotFoundException;

    Uni<User> getUserByEmail(String email) throws ModelNotFoundException;

    Uni<Set<User>> getUsers(Integer firstResult, Integer maxResult);

    Uni<Long> getUsersCount();

    @Deprecated
    Uni<String> createUser(String email, String username) throws ModelDuplicateException;

    @Deprecated
    Uni<String> createUser(User user) throws ModelDuplicateException;

    @Deprecated
    void removeUser(String id) throws ModelNotFoundException;

    @Deprecated
    Uni<UserModel> updateUser(User user) throws ModelNotFoundException;
}
