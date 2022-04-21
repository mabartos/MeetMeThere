package org.mabartos.meetmethere.service.core;

/*import javax.persistence.EntityManager;
import org.mabartos.meetmethere.api.model.jpa.provider.JpaEventProvider;*/

import org.mabartos.meetmethere.api.model.jpa.provider.JpaEventInvitationProviderFactory;
import org.mabartos.meetmethere.api.model.jpa.provider.JpaEventProviderFactory;
import org.mabartos.meetmethere.api.model.jpa.provider.JpaUserProviderFactory;
import org.mabartos.meetmethere.api.provider.EventProvider;
import org.mabartos.meetmethere.api.provider.InvitationProvider;
import org.mabartos.meetmethere.api.provider.UserProvider;
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

    @Override
    public UserProvider users() {
        return new JpaUserProviderFactory().create(this);
    }

    @Override
    public EventProvider events() {
        return new JpaEventProviderFactory().create(this);
    }

    @Override
    public InvitationProvider invitations() {
        return new JpaEventInvitationProviderFactory().create(this);
    }

    @Override
    public EntityManager entityManager() {
        return em;
    }
}
