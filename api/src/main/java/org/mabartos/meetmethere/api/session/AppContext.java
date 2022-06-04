package org.mabartos.meetmethere.api.session;

public interface AppContext {

    Long getCurrentEventId();

    void setCurrentEvent(Long id);

    Long getCurrentInvitation();

    void setCurrentInvitation(Long id);
}
