package org.mabartos.meetmethere.api.provider;

import org.mabartos.meetmethere.api.model.UserModel;
import org.mabartos.meetmethere.api.model.exception.ModelDuplicateException;
import org.mabartos.meetmethere.api.model.exception.ModelNotFoundException;

import java.util.Set;

public interface UserProvider {

    UserModel getUserById(Long id);

    UserModel getUserByUsername(String username);

    UserModel getUserByEmail(String email);

    Set<UserModel> getUsers(int firstResult, int maxResults);

    long getUsersCount();

    UserModel createUser(String email, String username) throws ModelDuplicateException;

    UserModel createUser(UserModel user) throws ModelDuplicateException;

    void removeUser(Long id);

    UserModel updateUser(UserModel user);
}
