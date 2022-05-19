package org.mabartos.meetmethere.model.jpa.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
public class AddressEntity {
    private String country;

    private String city;

    private String zipCode;

    private String street;

    private String streetNumber;

    private Double longitude;

    private Double latitude;

    public AddressEntity() {
    }
}
