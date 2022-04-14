package org.mabartos.meetmethere.model.provider;

import org.mabartos.meetmethere.model.UserModel;

import java.util.Set;

public interface UserProvider {

    UserModel getUserById(Long id);

    UserModel getUserByUsername(String username);

    Set<UserModel> getUsers(int firstResult, int maxResults);

    long getUsersCount();

    void createUser(UserModel user);

    void removeUser(Long id);

    void updateUser(Long id, UserModel user);
}
