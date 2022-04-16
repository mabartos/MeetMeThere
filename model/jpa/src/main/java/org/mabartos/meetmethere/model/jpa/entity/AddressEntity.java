package org.mabartos.meetmethere.model.jpa.entity;

import org.mabartos.meetmethere.model.AddressModel;

import javax.persistence.Embeddable;

import static org.mabartos.meetmethere.UpdateUtil.update;

@Embeddable
public class AddressEntity extends AddressModel {
    public AddressEntity(String country) {
        super(country);
    }

    public AddressEntity() {
        super("");
    }

    public AddressEntity(AddressModel model) {
        super(model.getCountry());
        update(this::setCountry, model::getCountry);
        update(this::setCity, model::getCity);
        update(this::setZipCode, model::getZipCode);
        update(this::setStreet, model::getStreet);
        update(this::setStreetNumber, model::getStreetNumber);
        update(this::setCoordinates, model::getCoordinates);
    }
}
