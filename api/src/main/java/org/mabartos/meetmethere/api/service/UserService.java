package org.mabartos.meetmethere.api.service;

import io.smallrye.mutiny.Uni;
import org.mabartos.meetmethere.api.domain.User;
import org.mabartos.meetmethere.api.model.UserModel;
import org.mabartos.meetmethere.api.model.eventbus.EmailUsernameObject;
import org.mabartos.meetmethere.api.model.eventbus.PaginationObject;
import org.mabartos.meetmethere.api.model.eventbus.UserSet;
import org.mabartos.meetmethere.api.model.exception.ModelDuplicateException;
import org.mabartos.meetmethere.api.model.exception.ModelNotFoundException;

public interface UserService {
    String USER_CREATE_EVENT = "userCreateEvent";
    String USER_CREATE_BASIC_EVENT = "userCreateBasicEvent";
    String USER_UPDATE_EVENT = "userUpdateEvent";
    String USER_REMOVE_EVENT = "userRemoveEvent";
    String USER_GET_USERS_EVENT = "userGetMultipleEvent";
    String USER_GET_USER_EVENT = "userGetSingleEvent";
    String USER_GET_USERNAME_EVENT = "userGetByUsernameEvent";
    String USER_GET_EMAIL_EVENT = "userGetByEmailEvent";
    String USER_COUNT_EVENT = "userCountEvent";

    Uni<User> getUserById(Long id) throws ModelNotFoundException;

    Uni<User> getUserByUsername(String username) throws ModelNotFoundException;

    Uni<User> getUserByEmail(String email) throws ModelNotFoundException;

    Uni<UserSet> getUsers(PaginationObject paginationObject);

    Uni<Long> getUsersCount(Object ignore);

    Uni<Long> createUser(EmailUsernameObject object) throws ModelDuplicateException;

    Uni<Long> createUser(User user) throws ModelDuplicateException;

    void removeUser(Long id) throws ModelNotFoundException;

    Uni<UserModel> updateUser(User user) throws ModelNotFoundException;

}
