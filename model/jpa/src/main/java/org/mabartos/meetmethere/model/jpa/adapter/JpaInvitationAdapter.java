package org.mabartos.meetmethere.model.jpa.adapter;

import jakarta.persistence.EntityManager;
import org.mabartos.meetmethere.enums.ResponseType;
import org.mabartos.meetmethere.model.EventModel;
import org.mabartos.meetmethere.model.InvitationModel;
import org.mabartos.meetmethere.model.UserModel;
import org.mabartos.meetmethere.model.jpa.JpaModel;
import org.mabartos.meetmethere.model.jpa.entity.EventEntity;
import org.mabartos.meetmethere.model.jpa.entity.InvitationEntity;
import org.mabartos.meetmethere.model.jpa.entity.UserEntity;
import org.mabartos.meetmethere.session.MeetMeThereSession;

public class JpaInvitationAdapter implements InvitationModel, JpaModel<InvitationEntity> {
    private final MeetMeThereSession session;
    private final EntityManager em;
    private final InvitationEntity entity;

    public JpaInvitationAdapter(MeetMeThereSession session, EntityManager em, InvitationEntity entity) {
        this.session = session;
        this.em = em;
        this.entity = entity;
    }

    @Override
    public Long getId() {
        return getEntity().getId();
    }

    @Override
    public void setId(Long id) {
        getEntity().setId(id);
    }

    @Override
    public EventModel getEvent() {
        return new JpaEventAdapter(session, em, getEntity().getEvent());
    }

    @Override
    public void setEvent(EventModel event) {
        getEntity().setEvent(EventEntity.findById(event.getId()));
    }

    @Override
    public UserModel getSender() {
        return new JpaUserAdapter(session, em, getEntity().getSender());
    }

    @Override
    public void setSender(UserModel sender) {
        getEntity().setSender(UserEntity.findById(sender.getId()));
    }

    @Override
    public UserModel getReceiver() {
        return new JpaUserAdapter(session, em, getEntity().getReceiver());
    }

    @Override
    public void setReceiver(UserModel receiver) {
        getEntity().setReceiver(UserEntity.findById(receiver.getId()));
    }

    @Override
    public ResponseType getResponseType() {
        return getEntity().getResponseType();
    }

    @Override
    public void setResponseType(ResponseType responseType) {
        getEntity().setResponseType(responseType);
    }

    @Override
    public InvitationEntity getEntity() {
        return entity;
    }
}
