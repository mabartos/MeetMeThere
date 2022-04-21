package org.mabartos.meetmethere.api.model;

public interface AddressModel {

    String getCountry();

    void setCountry(String country);

    String getCity();

    void setCity(String city);

    String getZipCode();

    void setZipCode(String zipCode);

    String getStreet();

    void setStreet(String street);

    String getStreetNumber();

    void setStreetNumber(String streetNumber);

    Coordinates getCoordinates();

    void setCoordinates(Coordinates coordinates);
}
