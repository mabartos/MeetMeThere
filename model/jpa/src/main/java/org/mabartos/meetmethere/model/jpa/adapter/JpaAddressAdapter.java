package org.mabartos.meetmethere.model.jpa.adapter;

import jakarta.persistence.EntityManager;
import org.mabartos.meetmethere.model.AddressModel;
import org.mabartos.meetmethere.model.Coordinates;
import org.mabartos.meetmethere.model.jpa.JpaModel;
import org.mabartos.meetmethere.model.jpa.entity.AddressEntity;
import org.mabartos.meetmethere.session.MeetMeThereSession;

public class JpaAddressAdapter implements AddressModel, JpaModel<AddressEntity> {
    private final MeetMeThereSession session;
    private final EntityManager em;
    private final AddressEntity entity;

    public JpaAddressAdapter(MeetMeThereSession session, EntityManager em, AddressEntity entity) {
        this.session = session;
        this.em = em;
        this.entity = entity;
    }

    @Override
    public Long getId() {
        return getEntity().getId();
    }

    @Override
    public void setId(Long id) {
        getEntity().setId(id);
    }

    @Override
    public String getCountry() {
        return getEntity().getCountry();
    }

    @Override
    public void setCountry(String country) {
        getEntity().setCountry(country);
    }

    @Override
    public String getCity() {
        return getEntity().getCity();
    }

    @Override
    public void setCity(String city) {
        getEntity().setCity(city);
    }

    @Override
    public String getZipCode() {
        return getEntity().getZipCode();
    }

    @Override
    public void setZipCode(String zipCode) {
        getEntity().setZipCode(zipCode);
    }

    @Override
    public String getStreet() {
        return getEntity().getStreet();
    }

    @Override
    public void setStreet(String street) {
        getEntity().setStreet(street);
    }

    @Override
    public String getStreetNumber() {
        return getEntity().getStreetNumber();
    }

    @Override
    public void setStreetNumber(String streetNumber) {
        getEntity().setStreetNumber(streetNumber);
    }

    @Override
    public Coordinates getCoordinates() {
        return new Coordinates(getEntity().getLongitude(), getEntity().getLatitude());
    }

    @Override
    public void setCoordinates(Coordinates coordinates) {
        if (coordinates == null) return;
        getEntity().setLongitude(coordinates.getLongitude());
        getEntity().setLatitude(coordinates.getLatitude());
    }

    @Override
    public AddressEntity getEntity() {
        return entity;
    }
}
