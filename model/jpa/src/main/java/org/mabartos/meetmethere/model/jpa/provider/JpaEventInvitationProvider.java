package org.mabartos.meetmethere.model.jpa.provider;

import org.mabartos.meetmethere.api.model.EventModel;
import org.mabartos.meetmethere.api.model.InvitationModel;
import org.mabartos.meetmethere.api.model.UserModel;
import org.mabartos.meetmethere.api.model.exception.ModelDuplicateException;
import org.mabartos.meetmethere.api.model.exception.ModelNotFoundException;
import org.mabartos.meetmethere.api.provider.InvitationProvider;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;
import org.mabartos.meetmethere.model.jpa.adapter.JpaEventAdapter;
import org.mabartos.meetmethere.model.jpa.adapter.JpaInvitationAdapter;
import org.mabartos.meetmethere.model.jpa.entity.EventEntity;
import org.mabartos.meetmethere.model.jpa.entity.InvitationEntity;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mabartos.meetmethere.api.UpdateUtil.update;

@Transactional
public class JpaEventInvitationProvider implements InvitationProvider {
    private final MeetMeThereSession session;
    private final EntityManager em;

    public JpaEventInvitationProvider(MeetMeThereSession session, EntityManager em) {
        this.session = session;
        this.em = em;
    }

    @Override
    public InvitationModel getInvitationById(Long id) {
        final InvitationEntity entity = InvitationEntity.findById(id);
        if (entity == null) return null;
        return new JpaInvitationAdapter(session, em, entity);
    }

    @Override
    public Set<InvitationModel> getInvitationsForEvent(Long eventId) {
        EventEntity event = em.find(EventEntity.class, eventId);
        if (event == null) return Collections.emptySet();

        return event.getInvitations()
                .stream()
                .map(f -> new JpaInvitationAdapter(session, em, f))
                .collect(Collectors.toSet());

    }

    @Override
    public InvitationModel createInvitation(InvitationModel invitation) throws ModelDuplicateException {
        if (JpaInvitationAdapter.convertToEntity(invitation, em) != null) {
            throw new ModelDuplicateException("Duplicate Event invitation");
        }

        if (invitation.getSender() == null) throw new IllegalArgumentException("You need to specify sender");
        if (invitation.getReceiver() == null) throw new IllegalArgumentException("You need to specify receiver");

        InvitationEntity entity = new InvitationEntity();
        entity.setEvent(JpaEventAdapter.convertToEntity(invitation.getEvent(), em));
        entity.setSenderId(invitation.getSender().getId());
        entity.setReceiverId(invitation.getReceiver().getId());

        convertInvitationStaticAttributes(entity, invitation);

        em.persist(entity);
        em.flush();

        return new JpaInvitationAdapter(session, em, entity);
    }

    @Override
    public InvitationModel createInvitation(EventModel event, UserModel sender, UserModel receiver, String message) {
        if (sender == null) throw new IllegalArgumentException("You need to specify sender");
        if (receiver == null) throw new IllegalArgumentException("You need to specify receiver");

        InvitationEntity entity = new InvitationEntity();

        entity.setEvent(JpaEventAdapter.convertToEntity(event, em));
        entity.setSenderId(sender.getId());
        entity.setReceiverId(receiver.getId());

        if (message != null) {
            entity.setMessage(message);
        }

        em.persist(entity);
        em.flush();
        return new JpaInvitationAdapter(session, em, entity);
    }

    @Override
    public InvitationModel createInvitation(EventModel event, UserModel sender, UserModel receiver) {
        return createInvitation(event, sender, receiver, null);
    }

    @Override
    public void createInvitations(EventModel event, UserModel sender, Set<UserModel> receivers, String message) {
        if (sender == null) throw new IllegalArgumentException("You need to specify sender");
        if (receivers == null || receivers.isEmpty())
            throw new IllegalArgumentException("You need to specify receiver");

        EventEntity eventEntity = JpaEventAdapter.convertToEntity(event, em);

        if (eventEntity == null) {
            throw new IllegalArgumentException("Cannot create invitations");
        }

        for (UserModel receiver : receivers) {
            InvitationEntity entity = new InvitationEntity();

            entity.setEvent(eventEntity);
            entity.setSenderId(sender.getId());
            entity.setReceiverId(receiver.getId());

            em.persist(entity);
        }

        em.flush();
    }

    @Override
    public void removeInvitation(Long id) throws ModelNotFoundException {
        if (!InvitationEntity.deleteById(id)) throw new ModelNotFoundException("Cannot find Event invitation");
    }

    @Override
    public InvitationModel updateInvitation(InvitationModel invitation) {
        InvitationEntity entity = Optional.ofNullable(JpaInvitationAdapter.convertToEntity(invitation, em))
                .orElseThrow(() -> new IllegalArgumentException("Cannot update Event invitation"));

        convertInvitation(entity, invitation);
        em.merge(entity);
        em.flush();

        return new JpaInvitationAdapter(session, em, entity);
    }

    public void convertInvitation(InvitationEntity entity, InvitationModel model) {
        update(entity::setEvent, () -> JpaEventAdapter.convertToEntity(model.getEvent(), em));
        update(entity::setSenderId, () -> model.getSender().getId());
        update(entity::setReceiverId, () -> model.getReceiver().getId());
        convertInvitationStaticAttributes(entity, model);
    }

    private void convertInvitationStaticAttributes(InvitationEntity entity, InvitationModel model) {
        update(entity::setMessage, model::getMessage);
    }
}
