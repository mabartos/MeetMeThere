package org.mabartos.meetmethere.service.core;

/*import javax.persistence.EntityManager;
import org.mabartos.meetmethere.api.model.jpa.provider.JpaEventProvider;*/

import io.vertx.mutiny.core.eventbus.EventBus;
import org.mabartos.meetmethere.api.provider.EventProvider;
import org.mabartos.meetmethere.api.provider.InvitationProvider;
import org.mabartos.meetmethere.api.provider.UserProvider;
import org.mabartos.meetmethere.api.service.AuthService;
import org.mabartos.meetmethere.api.service.EventInvitationService;
import org.mabartos.meetmethere.api.service.EventService;
import org.mabartos.meetmethere.api.service.UserService;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;
import org.mabartos.meetmethere.service.core.factory.AuthServiceFactory;
import org.mabartos.meetmethere.service.core.factory.EventServiceFactory;
import org.mabartos.meetmethere.service.core.factory.InvitationServiceFactory;
import org.mabartos.meetmethere.service.core.factory.UserServiceFactory;
import org.mabartos.meetmethere.service.core.store.EventStoreFactory;
import org.mabartos.meetmethere.service.core.store.InvitationStoreFactory;
import org.mabartos.meetmethere.service.core.store.UserStoreFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@RequestScoped
@Transactional
public class DefaultMeetMeThereSession implements MeetMeThereSession {

    @Inject
    EntityManager em;

    @Inject
    EventBus eventBus;

    @Override
    public AuthService auth() {
        return new AuthServiceFactory().create(this);
    }

    @Deprecated
    @Override
    public UserService users() {
        return new UserServiceFactory().create(this);
    }

    @Override
    public UserProvider userStorage() {
        return new UserStoreFactory().create(this);
    }

    @Override
    public EventService events() {
        return new EventServiceFactory().create(this);
    }

    @Override
    public EventProvider eventStorage() {
        return new EventStoreFactory().create(this);
    }

    @Override
    public EventInvitationService invitations() {
        return new InvitationServiceFactory().create(this);
    }

    @Override
    public InvitationProvider invitationStorage() {
        return new InvitationStoreFactory().create(this);
    }

    @Override
    public EntityManager entityManager() {
        return em;
    }

    @Override
    public EventBus eventBus() {
        return eventBus;
    }
}
