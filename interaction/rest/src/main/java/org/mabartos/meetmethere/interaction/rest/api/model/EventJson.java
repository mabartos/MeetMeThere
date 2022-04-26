package org.mabartos.meetmethere.interaction.rest.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.mabartos.meetmethere.api.domain.Event;
import org.mabartos.meetmethere.interaction.rest.api.model.mapper.AddressJsonDomainMapper;
import org.mapstruct.factory.Mappers;

import javax.enterprise.inject.Default;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventJson extends Event {
    private static final AddressJsonDomainMapper mapper = Mappers.getMapper(AddressJsonDomainMapper.class);

    @JsonCreator
    public EventJson(@JsonProperty("title") String title,
                     @JsonProperty("createdById") Long createdById,
                     @JsonProperty("createdByFullName") String createdByFullName,
                     @JsonProperty("venue") AddressJson venue) {
        super(title, createdById, createdByFullName);
        super.setVenue(mapper.toDomain(venue));
    }
}
