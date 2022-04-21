package org.mabartos.meetmethere.api.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class Address implements Serializable {
    private String country;

    private String city;

    private String zipCode;

    private String street;

    private String streetNumber;

    private Double longitude;

    private Double latitude;

    public Address(String country) {
        this.country = country;
    }
}
