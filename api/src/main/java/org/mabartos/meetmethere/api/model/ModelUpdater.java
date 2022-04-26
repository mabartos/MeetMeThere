package org.mabartos.meetmethere.api.model;

import org.mabartos.meetmethere.api.domain.Address;
import org.mabartos.meetmethere.api.domain.Event;
import org.mabartos.meetmethere.api.domain.EventInvitation;
import org.mabartos.meetmethere.api.domain.User;

import static org.mabartos.meetmethere.api.UpdateUtil.update;

public class ModelUpdater {

    public static void updateModel(User user, UserModel model) {
        update(model::setUsername, user::getUsername);
        update(model::setEmail, user::getEmail);
        update(model::setFirstName, user::getFirstName);
        update(model::setLastName, user::getLastName);
        update(model::setAttributes, user::getAttributes);
    }

    public static void updateModel(Event event, EventModel model) {
        update(model::setEventTitle, event::getTitle);
        update(model::setDescription, event::getDescription);
        update(model::setPublic, event::isPublic);
        update(model::setUpdatedAt, event::getUpdatedAt);
        update(model::setStartTime, event::getStartTime);
        update(model::setEndTime, event::getEndTime);
        update(model::setAttributes, event::getAttributes);

        if (model.getVenue() != null && event.getVenue() != null) {
            updateModel(event.getVenue(), model.getVenue());
        }
    }

    public static void updateModel(EventInvitation invitation, InvitationModel model) {
        update(model::setMessage, invitation::getMessage);
        update(model::setResponseType, invitation::getResponseType);
    }

    public static void updateModel(Address address, AddressModel model) {
        update(model::setCountry, address::getCountry);
        update(model::setZipCode, address::getZipCode);
        update(model::setCity, address::getCity);
        update(model::setStreet, address::getStreet);
        update(model::setStreetNumber, address::getStreetNumber);
        update(model::setCoordinates, () -> new Coordinates(address.getLongitude(), address.getLatitude()));

    }
}
