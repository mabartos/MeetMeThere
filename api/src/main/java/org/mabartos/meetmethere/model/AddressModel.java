package org.mabartos.meetmethere.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddressModel {

    private String country;

    private String city;

    private String zipCode;

    private String street;

    private String streetNumber;

    private Coordinates coordinates;

    public AddressModel(String country) {
        this.country = country;
    }
}
