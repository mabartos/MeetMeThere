package org.mabartos.meetmethere.model.provider;

import org.mabartos.meetmethere.model.UserModel;
import org.mabartos.meetmethere.model.exception.ModelDuplicateException;
import org.mabartos.meetmethere.model.exception.ModelNotFoundException;

import java.util.Set;

public interface UserProvider {

    UserModel getUserById(Long id);

    UserModel getUserByUsername(String username);

    UserModel getUserByEmail(String email);

    Set<UserModel> getUsers(int firstResult, int maxResults);

    long getUsersCount();

    UserModel createUser(String email, String username) throws ModelDuplicateException;

    UserModel createUser(UserModel user) throws ModelDuplicateException;

    void removeUser(Long id) throws ModelNotFoundException;

    UserModel updateUser(UserModel user);
}
