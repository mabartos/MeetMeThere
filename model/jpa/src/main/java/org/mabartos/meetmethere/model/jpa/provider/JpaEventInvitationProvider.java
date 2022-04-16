package org.mabartos.meetmethere.model.jpa.provider;

import org.mabartos.meetmethere.model.EventModel;
import org.mabartos.meetmethere.model.InvitationModel;
import org.mabartos.meetmethere.model.UserModel;
import org.mabartos.meetmethere.model.exception.ModelDuplicateException;
import org.mabartos.meetmethere.model.exception.ModelNotFoundException;
import org.mabartos.meetmethere.model.jpa.adapter.JpaInvitationAdapter;
import org.mabartos.meetmethere.model.jpa.entity.EventEntity;
import org.mabartos.meetmethere.model.jpa.entity.InvitationEntity;
import org.mabartos.meetmethere.model.jpa.entity.UserEntity;
import org.mabartos.meetmethere.model.provider.InvitationProvider;
import org.mabartos.meetmethere.session.MeetMeThereSession;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mabartos.meetmethere.UpdateUtil.update;

public class JpaEventInvitationProvider implements InvitationProvider {
    private final MeetMeThereSession session;
    private final EntityManager em;

    public JpaEventInvitationProvider(MeetMeThereSession session, EntityManager em) {
        this.session = session;
        this.em = em;
    }

    @Override
    public InvitationModel getInvitationById(Long id) {
        return InvitationEntity.findById(id);
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
        if (InvitationEntity.<InvitationEntity>findByIdOptional(invitation.getId()).isPresent()) {
            throw new ModelDuplicateException("Duplicate Event invitation");
        }

        InvitationEntity entity = new InvitationEntity();
        entity.setEvent(em.find(EventEntity.class, invitation.getEvent().getId()));
        entity.setSender(em.find(UserEntity.class, invitation.getSender().getId()));
        entity.setReceiver(em.find(UserEntity.class, invitation.getReceiver().getId()));

        convertInvitationStaticAttributes(entity, invitation);

        em.persist(entity);
        em.flush();

        return new JpaInvitationAdapter(session, em, entity);
    }

    @Override
    public InvitationModel createInvitation(EventModel event, UserModel sender, UserModel receiver) {
        InvitationEntity entity = new InvitationEntity();

        entity.setEvent(em.find(EventEntity.class, event.getId()));
        entity.setSender(em.find(UserEntity.class, sender.getId()));
        entity.setReceiver(em.find(UserEntity.class, receiver.getId()));

        em.persist(entity);
        em.flush();
        return new JpaInvitationAdapter(session, em, entity);
    }

    @Override
    public void createInvitations(EventModel event, UserModel sender, Set<UserModel> receivers, String message) {
        EventEntity eventEntity = em.find(EventEntity.class, event.getId());
        UserEntity senderEntity = em.find(UserEntity.class, sender.getId());

        if (eventEntity == null || senderEntity == null) {
            throw new IllegalArgumentException("Cannot create invitations");
        }

        for (UserModel receiver : receivers) {
            InvitationEntity entity = new InvitationEntity();

            entity.setEvent(eventEntity);
            entity.setSender(senderEntity);
            entity.setReceiver(em.find(UserEntity.class, receiver.getId()));

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
        InvitationEntity entity = InvitationEntity.<InvitationEntity>findByIdOptional(invitation.getId())
                .orElseThrow(() -> new IllegalArgumentException("Cannot update Event invitation"));

        convertInvitation(entity, invitation);
        em.merge(entity);
        em.flush();

        return new JpaInvitationAdapter(session, em, entity);
    }

    public void convertInvitation(InvitationEntity entity, InvitationModel model) {
        update(entity::setEvent, () -> em.find(EventEntity.class, model.getEvent().getId()));
        update(entity::setSender, () -> em.find(UserEntity.class, model.getSender().getId()));
        update(entity::setReceiver, () -> em.find(UserEntity.class, model.getReceiver().getId()));
        convertInvitationStaticAttributes(entity, model);
    }

    private void convertInvitationStaticAttributes(InvitationEntity entity, InvitationModel model) {
        update(entity::setMessage, model::getMessage);
        update(entity::setResponseType, model::getResponseType);
    }
}
