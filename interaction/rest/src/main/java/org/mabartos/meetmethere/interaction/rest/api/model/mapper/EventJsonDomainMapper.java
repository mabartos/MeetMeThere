package org.mabartos.meetmethere.interaction.rest.api.model.mapper;

import org.mabartos.meetmethere.api.domain.Event;
import org.mabartos.meetmethere.interaction.rest.api.model.EventJson;
import org.mapstruct.Mapper;

@Mapper
public interface EventJsonDomainMapper {
    Event toDomain(EventJson json);

    EventJson toJson(Event domain);
}
