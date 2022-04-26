package org.mabartos.meetmethere.interaction.rest.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.mabartos.meetmethere.api.domain.Address;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressJson extends Address {

    @JsonCreator
    public AddressJson(@JsonProperty("country") String country) {
        super(country);
    }

}
