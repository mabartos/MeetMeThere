package org.mabartos.meetmethere.api.session;

import io.vertx.mutiny.core.eventbus.EventBus;
import org.mabartos.meetmethere.api.provider.EventProvider;
import org.mabartos.meetmethere.api.provider.InvitationProvider;
import org.mabartos.meetmethere.api.provider.UserProvider;
import org.mabartos.meetmethere.api.service.EventService;

import javax.persistence.EntityManager;

public interface MeetMeThereSession {

    UserProvider users();

    EventService events();

    EventProvider eventsStorage();

    InvitationProvider invitations();

    EntityManager entityManager();

    EventBus eventBus();
}
