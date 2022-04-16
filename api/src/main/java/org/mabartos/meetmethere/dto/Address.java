package org.mabartos.meetmethere.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.mabartos.meetmethere.model.AddressModel;
import org.mabartos.meetmethere.model.Coordinates;

import java.io.Serializable;

@Setter
@Getter
public class Address extends AddressModel implements Serializable {

    private Double longitude;

    private Double latitude;

    @JsonIgnore
    public Coordinates getCoordinates() {
        return super.getCoordinates();
    }

    public Address(String country) {
        super(country);
    }
}
