package org.mabartos.meetmethere.interaction.rest.api.model;

import org.mabartos.meetmethere.api.domain.Event;
import org.mabartos.meetmethere.api.domain.EventInvitation;
import org.mabartos.meetmethere.api.domain.User;
import org.mabartos.meetmethere.api.model.EventModel;
import org.mabartos.meetmethere.api.model.InvitationModel;
import org.mabartos.meetmethere.api.model.UserModel;

import static org.mabartos.meetmethere.api.UpdateUtil.update;

public class JsonToModel {

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
    }

    public static void updateModel(EventInvitation invitation, InvitationModel model) {
        update(model::setMessage, invitation::getMessage);
        update(model::setResponseType, invitation::getResponseType);
    }
}