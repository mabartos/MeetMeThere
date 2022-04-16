package org.mabartos.meetmethere.session;

import org.mabartos.meetmethere.model.provider.AddressProvider;
import org.mabartos.meetmethere.model.provider.EventProvider;
import org.mabartos.meetmethere.model.provider.InvitationProvider;
import org.mabartos.meetmethere.model.provider.UserProvider;

import javax.persistence.EntityManager;

public interface MeetMeThereSession {

    UserProvider users();

    EventProvider events();

    InvitationProvider invitations();

    AddressProvider addresses();

    EntityManager entityManager();
}
