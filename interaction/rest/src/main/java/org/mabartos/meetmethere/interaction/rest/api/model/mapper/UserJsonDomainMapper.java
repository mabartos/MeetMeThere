package org.mabartos.meetmethere.interaction.rest.api.model.mapper;

import org.mabartos.meetmethere.api.domain.User;
import org.mabartos.meetmethere.interaction.rest.api.model.UserJson;
import org.mapstruct.Mapper;

@Mapper
public interface UserJsonDomainMapper {

    User toDomain(UserJson json);

    UserJson toJson(User user);

}
