package org.mabartos.meetmethere.service.core;

/*import javax.persistence.EntityManager;
import org.mabartos.meetmethere.model.jpa.provider.JpaEventProvider;*/
import org.mabartos.meetmethere.model.provider.AddressProvider;
import org.mabartos.meetmethere.model.provider.EventProvider;
import org.mabartos.meetmethere.model.provider.InvitationProvider;
import org.mabartos.meetmethere.model.provider.UserProvider;
import org.mabartos.meetmethere.session.MeetMeThereSession;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@RequestScoped
@Transactional
public class DefaultMeetMeThereSession implements MeetMeThereSession {

    @Inject
    BeanManager beanManager;

    private final ConcurrentMap<Class<?>, Object> providers = new ConcurrentHashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getProvider(Class<T> clazz) {
        return (T) providers.computeIfAbsent(clazz, item -> {
            Set<Bean<?>> beans = beanManager.getBeans(clazz);
            if (beans.iterator().hasNext()) {
                Bean<?> bean = beans.iterator().next();
                CreationalContext<?> context = beanManager.createCreationalContext(bean);
                return beanManager.getReference(bean, clazz, context);
            }
            return null;
        });
    }

    @Override
    public UserProvider users() {
        return getProvider(UserProvider.class);
    }

    @Override
    public EventProvider events() {
        return getProvider(EventProvider.class);
    }

    @Override
    public InvitationProvider invitations() {
        return getProvider(InvitationProvider.class);
    }

    @Override
    public AddressProvider addresses() {
        return getProvider(AddressProvider.class);
    }
}
