package org.mabartos.meetmethere.service.core;

/*import javax.persistence.EntityManager;
import org.mabartos.meetmethere.model.jpa.provider.JpaEventProvider;*/

import org.mabartos.meetmethere.model.jpa.provider.JpaEventInvitationProviderFactory;
import org.mabartos.meetmethere.model.jpa.provider.JpaEventProviderFactory;
import org.mabartos.meetmethere.model.jpa.provider.JpaUserProviderFactory;
import org.mabartos.meetmethere.model.provider.EventProvider;
import org.mabartos.meetmethere.model.provider.InvitationProvider;
import org.mabartos.meetmethere.model.provider.UserProvider;
import org.mabartos.meetmethere.session.MeetMeThereSession;

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
