package org.mabartos.meetmethere.service.core.session;

/*import javax.persistence.EntityManager;
import org.mabartos.meetmethere.api.model.jpa.provider.JpaEventProvider;*/

import io.vertx.mutiny.core.eventbus.EventBus;
import org.mabartos.meetmethere.api.provider.EventProvider;
import org.mabartos.meetmethere.api.provider.InvitationProvider;
import org.mabartos.meetmethere.api.provider.UserProvider;
import org.mabartos.meetmethere.api.service.AuthService;
import org.mabartos.meetmethere.api.service.EventInvitationService;
import org.mabartos.meetmethere.api.service.EventService;
import org.mabartos.meetmethere.api.service.UserService;
import org.mabartos.meetmethere.api.session.AppContext;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;
import org.mabartos.meetmethere.service.core.factory.AuthServiceFactory;
import org.mabartos.meetmethere.service.core.factory.EventServiceFactory;
import org.mabartos.meetmethere.service.core.factory.InvitationServiceFactory;
import org.mabartos.meetmethere.service.core.factory.UserServiceFactory;
import org.mabartos.meetmethere.service.core.store.EventStoreFactory;
import org.mabartos.meetmethere.service.core.store.InvitationStoreFactory;
import org.mabartos.meetmethere.service.core.store.UserStoreFactory;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@RequestScoped
@Transactional
public class DefaultMeetMeThereSession implements MeetMeThereSession {
    private final ConcurrentMap<Class<?>, Object> providers = new ConcurrentHashMap<>();

    @Inject
    BeanManager beanManager;

    @Inject
    EntityManager em;

    @Inject
    EventBus eventBus;

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getProvider(Class<T> clazz) {
        return (T) providers.computeIfAbsent(clazz, item -> {
            final Set<Bean<?>> beans = beanManager.getBeans(clazz);
            if (beans.iterator().hasNext()) {
                final Bean<?> bean = beans.iterator().next();
                final CreationalContext<?> context = beanManager.createCreationalContext(bean);
                return beanManager.getReference(bean, clazz, context);
            }
            return null;
        });
    }

    @Override
    public AppContext context() {
        return new DefaultAppContext(this);
    }

    @Override
    public AuthService auth() {
        return new AuthServiceFactory().create(this);
    }

    @Deprecated
    @Override
    public UserService users() {
        return new UserServiceFactory().create(this);
    }

    @Override
    public UserProvider userStorage() {
        return new UserStoreFactory().create(this);
    }

    @Override
    public EventService events() {
        return new EventServiceFactory().create(this);
    }

    @Override
    public EventProvider eventStorage() {
        return new EventStoreFactory().create(this);
    }

    @Override
    public EventInvitationService invitations() {
        return new InvitationServiceFactory().create(this);
    }

    @Override
    public InvitationProvider invitationStorage() {
        return new InvitationStoreFactory().create(this);
    }

    @Override
    public EntityManager entityManager() {
        return em;
    }

    @Override
    public EventBus eventBus() {
        return eventBus;
    }
}
