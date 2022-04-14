package org.mabartos.meetmethere.model.provider;

import org.mabartos.meetmethere.model.InvitationModel;

public interface InvitationProvider {

    InvitationModel getInvitationById(Long id);

    void createInvitation(InvitationModel invitation);

    void removeInvitation(Long id);

    void updateInvitation(Long id, InvitationModel invitation);
}
