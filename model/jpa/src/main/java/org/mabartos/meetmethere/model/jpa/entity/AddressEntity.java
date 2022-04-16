package org.mabartos.meetmethere.model.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ADDRESS")
@NoArgsConstructor
@Setter
@Getter
public class AddressEntity extends BaseEntity {

    private String country;

    private String city;

    private String zipCode;

    private String street;

    private String streetNumber;

    private Double longitude;

    private Double latitude;
}
