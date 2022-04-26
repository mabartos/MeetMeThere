package org.mabartos.meetmethere.api.provider;

import org.mabartos.meetmethere.api.model.EventModel;
import org.mabartos.meetmethere.api.model.InvitationModel;
import org.mabartos.meetmethere.api.model.UserModel;
import org.mabartos.meetmethere.api.model.exception.ModelDuplicateException;
import org.mabartos.meetmethere.api.model.exception.ModelNotFoundException;

import java.util.Set;

public interface InvitationProvider {

    InvitationModel getInvitationById(Long id);

    Set<InvitationModel> getInvitationsForEvent(Long eventId);

    InvitationModel createInvitation(InvitationModel invitation) throws ModelDuplicateException;

    InvitationModel createInvitation(EventModel event, UserModel sender, UserModel receiver);

    InvitationModel createInvitation(EventModel event, UserModel sender, UserModel receiver, String message);

    void createInvitations(EventModel event, UserModel sender, Set<UserModel> receivers, String message);

    void removeInvitation(Long id) throws ModelNotFoundException;

    InvitationModel updateInvitation(InvitationModel invitation);
}
