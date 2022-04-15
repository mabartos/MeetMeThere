package org.mabartos.meetmethere;

import org.mabartos.meetmethere.dto.Address;
import org.mabartos.meetmethere.dto.Event;
import org.mabartos.meetmethere.dto.EventInvitation;
import org.mabartos.meetmethere.dto.User;
import org.mabartos.meetmethere.model.AddressModel;
import org.mabartos.meetmethere.model.EventModel;
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

        update(user::setOrganizedEvents, () -> model.getOrganizedEvents().stream().map(ModelToDto::toDto).collect(Collectors.toSet()));

        return user;
    }

    public static Event toDto(EventModel model) {
        Event event = new Event(model.getEventTitle(), toDto(model.getCreatedBy()), model.getCreatedAt());
        update(event::setId, model::getId);
        update(event::setDescription, model::getDescription);
        update(event::setPublic, model::isPublic);
        update(event::setVenue, () -> toDto(model.getVenue()));
        update(event::setUpdatedAt, model::getUpdatedAt);
        update(event::setUpdatedBy, () -> toDto(model.getUpdatedBy()));

        update(event::setStartTime, model::getStartTime);
        update(event::setEndTime, model::getEndTime);

        update(event::setAttributes, model::getAttributes);

        update(event::setInvitations, () -> model.getInvitations().stream().map(ModelToDto::toDto).collect(Collectors.toSet()));
        update(event::setOrganizers, () -> model.getOrganizers().stream().map(ModelToDto::toDto).collect(Collectors.toSet()));

        return event;
    }

    public static Address toDto(AddressModel model) {
        Address address = new Address(model.getCountry());

        return address;
    }

    public static EventInvitation toDto(InvitationModel model) {
        EventInvitation eventInvitation = new EventInvitation(toDto(model.getEvent()), toDto(model.getSender()), toDto(model.getReceiver()));
        eventInvitation.setResponseType(model.getResponseType());
        return eventInvitation;
    }


}
