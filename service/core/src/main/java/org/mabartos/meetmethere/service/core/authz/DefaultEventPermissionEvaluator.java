package org.mabartos.meetmethere.service.core.authz;

import org.mabartos.meetmethere.api.authz.EventPermissionEvaluator;
import org.mabartos.meetmethere.api.authz.InvitationPermissionEvaluator;
import org.mabartos.meetmethere.api.domain.Event;
import org.mabartos.meetmethere.api.mapper.ModelToDomain;
import org.mabartos.meetmethere.api.model.EventModel;
import org.mabartos.meetmethere.api.model.UserModel;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;

public class DefaultEventPermissionEvaluator implements EventPermissionEvaluator {
    private final MeetMeThereSession session;

    public DefaultEventPermissionEvaluator(MeetMeThereSession session) {
        this.session = session;
    }

    @Override
    public boolean canView() {
        return true;
    }

    @Override
    public boolean canView(Event event) {
        if (event == null) return false;
        if (event.isPublic()) return canView();
        return hasSufficientPermissions(event);
    }

    @Override
    public boolean canViewId(Long id) {
        final EventModel event = session.eventStorage().getEventById(id);
        if (event == null) return false;
        return canView(ModelToDomain.toDomain(event));
    }

    @Override
    public boolean canManage(Event event) {
        return hasSufficientPermissions(event);
    }

    @Override
    public boolean canManageId(Long id) {
        final EventModel event = session.eventStorage().getEventById(id);
        if (event == null) return false;
        return canManage(ModelToDomain.toDomain(event));
    }

    private static boolean isCreatorOrOrganizator(String userId, Event event) {
        return userId.equals(event.getCreatedById()) || (event.getOrganizersId() != null && event.getOrganizersId().contains(userId));
    }

    private boolean hasSufficientPermissions(Event event) {
        if (!session.auth().isAuthenticated()) return false;

        final UserModel authUser = session.auth().getAuthenticatedUser();
        if (authUser == null) return false;

        return session.auth().isAdmin() || isCreatorOrOrganizator(authUser.getId(), event);
    }

    @Override
    public InvitationPermissionEvaluator invitations() {
        return new DefaultInvitationPermissionEvaluator(session);
    }
}
