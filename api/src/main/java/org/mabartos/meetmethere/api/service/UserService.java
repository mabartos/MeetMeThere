package org.mabartos.meetmethere.api.service;

import io.smallrye.mutiny.Uni;
import org.mabartos.meetmethere.api.domain.User;
import org.mabartos.meetmethere.api.model.UserModel;
import org.mabartos.meetmethere.api.model.exception.ModelDuplicateException;
import org.mabartos.meetmethere.api.model.exception.ModelNotFoundException;

import java.util.Set;

public interface UserService {
    Uni<User> getUserById(Long id) throws ModelNotFoundException;

    Uni<User> getUserByUsername(String username) throws ModelNotFoundException;

    Uni<User> getUserByEmail(String email) throws ModelNotFoundException;

    Uni<Set<User>> getUsers(Integer firstResult, Integer maxResult);

    Uni<Long> getUsersCount();

    Uni<Long> createUser(String email, String username) throws ModelDuplicateException;

    Uni<Long> createUser(User user) throws ModelDuplicateException;

    void removeUser(Long id) throws ModelNotFoundException;

    Uni<UserModel> updateUser(User user) throws ModelNotFoundException;

}
