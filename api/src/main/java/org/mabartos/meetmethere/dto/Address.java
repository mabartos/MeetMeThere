package org.mabartos.meetmethere.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Address {

    private Long id;

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
