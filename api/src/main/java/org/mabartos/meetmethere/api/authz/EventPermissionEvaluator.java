package org.mabartos.meetmethere.api.authz;

import org.mabartos.meetmethere.api.domain.Event;

public interface EventPermissionEvaluator extends PermissionEvaluator<Event, Long> {

    InvitationPermissionEvaluator invitations();
}
