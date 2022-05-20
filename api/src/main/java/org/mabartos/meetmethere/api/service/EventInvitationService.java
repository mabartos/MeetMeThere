package org.mabartos.meetmethere.api.service;

import io.smallrye.mutiny.Uni;
import org.mabartos.meetmethere.api.domain.EventInvitation;

import java.util.Set;

public interface EventInvitationService {
    Uni<Set<EventInvitation>> getInvitations(Long eventId);

    Uni<Long> createInvitation(EventInvitation invitation);

    void removeInvitations(Long eventId);

    Uni<Long> getInvitationsCount(Long eventId);

    Uni<EventInvitation> getInvitation(Long invitationId);

    void removeInvitation(Long invitationId);

    Uni<EventInvitation> updateInvitation(EventInvitation invitation);
}
