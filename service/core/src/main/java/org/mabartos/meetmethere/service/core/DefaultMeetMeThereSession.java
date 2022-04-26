package org.mabartos.meetmethere.service.core;

/*import javax.persistence.EntityManager;
import org.mabartos.meetmethere.api.model.jpa.provider.JpaEventProvider;*/

import io.vertx.mutiny.core.eventbus.EventBus;
import org.mabartos.meetmethere.api.model.jpa.provider.JpaEventInvitationProviderFactory;
import org.mabartos.meetmethere.api.model.jpa.provider.JpaEventProviderFactory;
import org.mabartos.meetmethere.api.model.jpa.provider.JpaUserProviderFactory;
import org.mabartos.meetmethere.api.provider.EventProvider;
import org.mabartos.meetmethere.api.provider.InvitationProvider;
import org.mabartos.meetmethere.api.provider.UserProvider;
import org.mabartos.meetmethere.api.service.EventInvitationService;
import org.mabartos.meetmethere.api.service.EventService;
import org.mabartos.meetmethere.api.service.UserService;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

// TODO don't hardcode it
@RequestScoped
@Transactional
public class DefaultMeetMeThereSession implements MeetMeThereSession {

    @Inject
    EntityManager em;

    @Inject
    EventBus eventBus;

    @Override
    public UserService users() {
        return new DefaultUserService(this);
    }

    @Override
    public UserProvider userStorage() {
        return new JpaUserProviderFactory().create(this);
    }

    @Override
    public EventService events() {
        return new DefaultEventService(this);
    }

    @Override
    public EventProvider eventStorage() {
        return new JpaEventProviderFactory().create(this);
    }

    @Override
    public EventInvitationService invitations() {
        return new DefaultEventInvitationService(this);
    }

    @Override
    public InvitationProvider invitationStorage() {
        return new JpaEventInvitationProviderFactory().create(this);
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
