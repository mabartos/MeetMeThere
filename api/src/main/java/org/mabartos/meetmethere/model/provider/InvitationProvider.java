package org.mabartos.meetmethere.model.provider;

import org.mabartos.meetmethere.model.EventModel;
import org.mabartos.meetmethere.model.InvitationModel;
import org.mabartos.meetmethere.model.UserModel;
import org.mabartos.meetmethere.model.exception.ModelDuplicateException;
import org.mabartos.meetmethere.model.exception.ModelNotFoundException;

import java.util.Set;

public interface InvitationProvider {

    InvitationModel getInvitationById(Long id);

    Set<InvitationModel> getInvitationsForEvent(Long eventId);

    InvitationModel createInvitation(InvitationModel invitation) throws ModelDuplicateException;

    InvitationModel createInvitation(EventModel event, UserModel sender, UserModel receiver);

    void createInvitations(EventModel event, UserModel sender, Set<UserModel> receivers, String message);

    void removeInvitation(Long id) throws ModelNotFoundException;

    InvitationModel updateInvitation(InvitationModel invitation);
}
