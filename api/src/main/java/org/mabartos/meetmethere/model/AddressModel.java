package org.mabartos.meetmethere.model;

public interface AddressModel extends HasId<Long> {

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
