package org.mabartos.meetmethere.interaction.rest.api.model;

import org.mabartos.meetmethere.api.domain.Address;
import org.mabartos.meetmethere.api.domain.Event;
import org.mabartos.meetmethere.api.domain.EventInvitation;
import org.mabartos.meetmethere.api.domain.User;
import org.mabartos.meetmethere.api.model.AddressModel;
import org.mabartos.meetmethere.api.model.Coordinates;
import org.mabartos.meetmethere.api.model.EventModel;
import org.mabartos.meetmethere.api.model.HasId;
import org.mabartos.meetmethere.api.model.InvitationModel;
import org.mabartos.meetmethere.api.model.UserModel;

import java.util.stream.Collectors;

import static org.mabartos.meetmethere.api.UpdateUtil.update;

public class ModelToJson {

    public static User toJson(UserModel model) {
        User user = new User(model.getUsername(), model.getEmail());
        update(user::setId, model::getId);
        update(user::setFirstName, model::getFirstName);
        update(user::setLastName, model::getLastName);
        update(user::setAttributes, model::getAttributes);

        update(user::setOrganizedEventsId, () -> model.getOrganizedEvents().stream().map(HasId::getId).collect(Collectors.toSet()));
        return user;
    }

    public static Event toJson(EventModel model) {
        Event event = new Event(model.getEventTitle(), toJson(model.getCreatedBy()));
        update(event::setId, model::getId);
        update(event::setDescription, model::getDescription);
        update(event::setPublic, model::isPublic);
        update(event::setVenue, () -> toJson(model.getVenue()));

        update(event::setUpdatedAt, model::getUpdatedAt);
        update(event::setCreatedAt, model::getCreatedAt);
        final UserModel updatedBy = model.getUpdatedBy();
        update(event::setUpdatedById, updatedBy::getId);
        update(event::setUpdatedByName, () -> String.format("%s %s", updatedBy.getFirstName(), updatedBy.getLastName()));

        update(event::setStartTime, model::getStartTime);
        update(event::setEndTime, model::getEndTime);

        update(event::setAttributes, model::getAttributes);

        update(event::setInvitationsId, () -> model.getInvitations().stream().map(HasId::getId).collect(Collectors.toSet()));
        update(event::setOrganizersId, () -> model.getOrganizers().stream().map(HasId::getId).collect(Collectors.toSet()));

        return event;
    }

    public static Address toJson(AddressModel model) {
        Address address = new Address(model.getCountry());
        update(address::setCity, model::getCity);
        update(address::setZipCode, model::getZipCode);
        update(address::setStreet, model::getStreet);
        update(address::setStreetNumber, model::getStreetNumber);

        final Coordinates coordinates = model.getCoordinates();
        update(address::setLongitude, coordinates::getLongitude);
        update(address::setLatitude, coordinates::getLatitude);
        return address;
    }

    public static EventInvitation toJson(InvitationModel model) {
        EventInvitation eventInvitation = new EventInvitation(toJson(model.getEvent()), toJson(model.getSender()), toJson(model.getReceiver()));
        update(eventInvitation::setId, model::getId);
        update(eventInvitation::setResponseType, model::getResponseType);
        update(eventInvitation::setMessage, model::getMessage);
        return eventInvitation;
    }


}
