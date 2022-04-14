package org.mabartos.meetmethere.model.provider;

import org.mabartos.meetmethere.model.AddressModel;
import org.mabartos.meetmethere.model.Coordinates;

import java.util.Set;

public interface AddressProvider {

    Set<AddressModel> getAddressesByCoordinates(Coordinates coordinates);

    void createAddress(AddressModel address);

    void removeAddress(AddressModel address);
}
