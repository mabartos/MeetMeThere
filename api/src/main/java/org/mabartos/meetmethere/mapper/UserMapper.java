package org.mabartos.meetmethere.mapper;

import org.mabartos.meetmethere.dto.User;
import org.mabartos.meetmethere.model.UserModel;

import java.util.stream.Collectors;

public class UserMapper implements ModelDtoMapper<User, UserModel> {
    private static final EventMapper mapper = new EventMapper();

    @Override
    public UserModel toModel(User user, UserModel userModel) {
        update(userModel::setUsername, user::getUsername);
        update(userModel::setEmail, user::getEmail);
        update(userModel::setFirstName, user::getFirstName);
        update(userModel::setLastName, user::getLastName);
        update(userModel::setAttributes, user::getAttributes);

        //TODO Organized Events

        return userModel;
    }

    @Override
    public User toDto(UserModel userModel) {
        User user = new User(userModel.getUsername(), userModel.getEmail());
        update(user::setFirstName, userModel::getFirstName);
        update(user::setLastName, userModel::getLastName);
        update(user::setAttributes, userModel::getAttributes);

        update(user::setOrganizedEvents, () -> userModel
                .getOrganizedEvents()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toSet()));

        return user;
    }
}
