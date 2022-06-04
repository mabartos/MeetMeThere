package org.mabartos.meetmethere.service.core.authz;

import org.mabartos.meetmethere.api.authz.EventPermissionEvaluator;
import org.mabartos.meetmethere.api.authz.InvitationPermissionEvaluator;
import org.mabartos.meetmethere.api.domain.EventInvitation;
import org.mabartos.meetmethere.api.mapper.ModelToDomain;
import org.mabartos.meetmethere.api.model.InvitationModel;
import org.mabartos.meetmethere.api.model.UserModel;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;

public class DefaultInvitationPermissionEvaluator implements InvitationPermissionEvaluator {
    private final MeetMeThereSession session;
    private final EventPermissionEvaluator eventEvaluator;

    public DefaultInvitationPermissionEvaluator(MeetMeThereSession session) {
        this.session = session;
        this.eventEvaluator = session.auth().events();
    }

    @Override
    public boolean canView() {
        return eventEvaluator.canViewId(session.context().getCurrentEventId());
    }

    @Override
    public boolean canView(EventInvitation invitation) {
        return eventEvaluator.canViewId(invitation.getEventId()) && hasSufficientPermissions(invitation);
    }

    @Override
    public boolean canViewId(Long invitationId) {
        final InvitationModel invitationModel = session.invitationStorage().getInvitationById(invitationId);
        if (invitationModel == null) return false;
        return canView(ModelToDomain.toDomain(invitationModel));
    }

    @Override
    public boolean canManage(EventInvitation invitation) {
        return eventEvaluator.canManageId(invitation.getEventId()) && hasSufficientPermissions(invitation);
    }

    @Override
    public boolean canManageId(Long invitationId) {
        final InvitationModel invitationModel = session.invitationStorage().getInvitationById(invitationId);
        if (invitationModel == null) return false;
        return canManage(ModelToDomain.toDomain(invitationModel));
    }

    private boolean hasSufficientPermissions(EventInvitation invitation) {
        if (invitation == null || !session.auth().isAuthenticated()) return false;

        final UserModel authUser = session.auth().getAuthenticatedUser();
        if (authUser == null) return false;

        return session.auth().isAdmin() ||
                authUser.getId().equals(invitation.getSenderId()) ||
                authUser.getId().equals(invitation.getReceiverId());
    }
}
