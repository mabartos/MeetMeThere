package org.mabartos.meetmethere.model.jpa.adapter;

import io.smallrye.common.constraint.NotNull;
import org.mabartos.meetmethere.api.model.EventModel;
import org.mabartos.meetmethere.api.model.InvitationModel;
import org.mabartos.meetmethere.api.model.UserModel;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;
import org.mabartos.meetmethere.model.jpa.JpaModel;
import org.mabartos.meetmethere.model.jpa.entity.InvitationEntity;
import org.mabartos.meetmethere.model.jpa.util.JpaUtil;

import javax.persistence.EntityManager;
import java.util.Objects;

public class JpaInvitationAdapter implements InvitationModel, JpaModel<InvitationEntity> {
    private final MeetMeThereSession session;
    private final EntityManager em;
    private final InvitationEntity entity;

    public JpaInvitationAdapter(MeetMeThereSession session,
                                @NotNull EntityManager em,
                                @NotNull InvitationEntity entity) {
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
        getEntity().setEvent(JpaEventAdapter.convertToEntity(event, em));
    }

    @Override
    public UserModel getSender() {
        return session.userStorage().getUserById(getEntity().getSenderId());
    }

    @Override
    public void setSender(UserModel sender) {
        getEntity().setSenderId(sender.getId());
    }

    @Override
    public UserModel getReceiver() {
        return session.userStorage().getUserById(getEntity().getSenderId());
    }

    @Override
    public void setReceiver(UserModel receiver) {
        getEntity().setReceiverId(receiver.getId());
    }

    @Override
    public String getMessage() {
        return getEntity().getMessage();
    }

    @Override
    public void setMessage(String message) {
        getEntity().setMessage(message);
    }

    @Override
    public InvitationEntity getEntity() {
        return entity;
    }

    public static InvitationEntity convertToEntity(InvitationModel model, EntityManager em) {
        return JpaUtil.convertToEntity(model, em, JpaInvitationAdapter.class, InvitationEntity.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JpaInvitationAdapter)) return false;
        JpaInvitationAdapter that = (JpaInvitationAdapter) o;
        return Objects.equals(getEntity(), that.getEntity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEntity());
    }
}
