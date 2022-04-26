package org.mabartos.meetmethere.interaction.rest.api.model.mapper;

import org.mabartos.meetmethere.api.domain.EventInvitation;
import org.mabartos.meetmethere.interaction.rest.api.model.EventInvitationJson;
import org.mapstruct.Mapper;

@Mapper
public interface EventInvitationJsonDomainMapper {

    EventInvitation toDomain(EventInvitationJson json);

    EventInvitationJson toJson(EventInvitation domain);

}
