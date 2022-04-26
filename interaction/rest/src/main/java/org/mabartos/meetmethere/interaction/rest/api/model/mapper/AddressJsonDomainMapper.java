package org.mabartos.meetmethere.interaction.rest.api.model.mapper;

import org.mabartos.meetmethere.api.domain.Address;
import org.mabartos.meetmethere.interaction.rest.api.model.AddressJson;
import org.mapstruct.Mapper;

@Mapper
public interface AddressJsonDomainMapper {
    Address toDomain(AddressJson json);

    AddressJson toJson(Address address);
}
