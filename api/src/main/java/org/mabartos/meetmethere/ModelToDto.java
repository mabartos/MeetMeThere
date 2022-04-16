package org.mabartos.meetmethere;

import org.mabartos.meetmethere.dto.Address;
import org.mabartos.meetmethere.dto.Event;
import org.mabartos.meetmethere.dto.EventInvitation;
import org.mabartos.meetmethere.dto.User;
import org.mabartos.meetmethere.model.AddressModel;
import org.mabartos.meetmethere.model.EventModel;
import org.mabartos.meetmethere.model.HasId;
import org.mabartos.meetmethere.model.InvitationModel;
import org.mabartos.meetmethere.model.UserModel;

import java.util.stream.Collectors;

import static org.mabartos.meetmethere.UpdateUtil.update;

public class ModelToDto {

    public static User toDto(UserModel model) {
        User user = new User(model.getUsername(), model.getEmail());
        update(user::setId, model::getId);
        update(user::setFirstName, model::getFirstName);
        update(user::setLastName, model::getLastName);
        update(user::setAttributes, model::getAttributes);

        update(user::setOrganizedEventsId, () -> model.getOrganizedEvents().stream().map(HasId::getId).collect(Collectors.toSet()));
        return user;
    }

    public static Event toDto(EventModel model) {
        Event event = new Event(model.getEventTitle(), toDto(model.getCreatedBy()), model.getCreatedAt());
        update(event::setId, model::getId);
        update(event::setDescription, model::getDescription);
        update(event::setPublic, model::isPublic);
        update(event::setVenue, () -> toDto(model.getVenue()));

        update(event::setUpdatedAt, model::getUpdatedAt);
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

    public static Address toDto(AddressModel model) {
        Address address = new Address(model.getCountry());
        update(address::setCity, model::getCity);
        update(address::setZipCode, model::getZipCode);
        update(address::setStreet, model::getStreet);
        update(address::setStreetNumber, model::getStreetNumber);
        update(address::setCoordinates, model::getCoordinates);
        return address;
    }

    public static EventInvitation toDto(InvitationModel model) {
        EventInvitation eventInvitation = new EventInvitation(toDto(model.getEvent()), toDto(model.getSender()), toDto(model.getReceiver()));
        update(eventInvitation::setResponseType, model::getResponseType);
        update(eventInvitation::setMessage, model::getMessage);
        return eventInvitation;
    }


}
