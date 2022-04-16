package org.mabartos.meetmethere.model.jpa.adapter;

import org.mabartos.meetmethere.model.AddressModel;
import org.mabartos.meetmethere.model.Coordinates;
import org.mabartos.meetmethere.model.jpa.entity.AddressEntity;

import static org.mabartos.meetmethere.UpdateUtil.update;

public class JpaAddressAdapter implements AddressModel {
    private final AddressEntity entity;

    public JpaAddressAdapter(AddressEntity entity) {
        this.entity = entity;
    }

    @Override
    public String getCountry() {
        return entity.getCountry();
    }

    @Override
    public void setCountry(String country) {
        entity.setCountry(country);
    }

    @Override
    public String getCity() {
        return entity.getCity();
    }

    @Override
    public void setCity(String city) {
        entity.setCity(city);
    }

    @Override
    public String getZipCode() {
        return entity.getZipCode();
    }

    @Override
    public void setZipCode(String zipCode) {
        entity.setZipCode(zipCode);
    }

    @Override
    public String getStreet() {
        return entity.getStreet();
    }

    @Override
    public void setStreet(String street) {
        entity.setStreet(street);
    }

    @Override
    public String getStreetNumber() {
        return getStreetNumber();
    }

    @Override
    public void setStreetNumber(String streetNumber) {
        entity.setStreetNumber(streetNumber);
    }

    @Override
    public Coordinates getCoordinates() {
        return new Coordinates(entity.getLongitude(), entity.getLatitude());
    }

    @Override
    public void setCoordinates(Coordinates coordinates) {
        entity.setLongitude(coordinates.getLongitude());
        entity.setLatitude(coordinates.getLatitude());
    }

    public static AddressEntity toEntity(AddressModel model) {
        AddressEntity entity = new AddressEntity();
        update(entity::setCountry, model::getCountry);
        update(entity::setCity, model::getCity);
        update(entity::setZipCode, model::getZipCode);
        update(entity::setStreet, model::getStreet);
        update(entity::setStreetNumber, model::getStreetNumber);

        final Coordinates coordinates = model.getCoordinates();
        update(entity::setLongitude, coordinates::getLongitude);
        update(entity::setLatitude, coordinates::getLatitude);
        return entity;
    }

}
