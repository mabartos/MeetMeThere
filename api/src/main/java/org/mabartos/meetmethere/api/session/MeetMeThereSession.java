package org.mabartos.meetmethere.api.session;

import io.vertx.mutiny.core.eventbus.EventBus;
import org.mabartos.meetmethere.api.provider.EventProvider;
import org.mabartos.meetmethere.api.provider.InvitationProvider;
import org.mabartos.meetmethere.api.provider.UserProvider;
import org.mabartos.meetmethere.api.service.AuthService;
import org.mabartos.meetmethere.api.service.EventInvitationService;
import org.mabartos.meetmethere.api.service.EventService;
import org.mabartos.meetmethere.api.service.UserService;

import javax.persistence.EntityManager;

public interface MeetMeThereSession {

    AppContext context();

    AuthService auth();

    UserService users();

    UserProvider userStorage();

    EventService events();

    EventProvider eventStorage();

    EventInvitationService invitations();

    InvitationProvider invitationStorage();

    EntityManager entityManager();

    EventBus eventBus();
}
