package org.mabartos.meetmethere.api.session;

import org.mabartos.meetmethere.api.provider.EventProvider;
import org.mabartos.meetmethere.api.provider.InvitationProvider;
import org.mabartos.meetmethere.api.provider.UserProvider;

import javax.persistence.EntityManager;

public interface MeetMeThereSession {

    UserProvider users();

    EventProvider events();

    InvitationProvider invitations();

    EntityManager entityManager();
}
