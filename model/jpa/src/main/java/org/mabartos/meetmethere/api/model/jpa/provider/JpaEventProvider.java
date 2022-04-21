package org.mabartos.meetmethere.api.model.jpa.provider;

import org.mabartos.meetmethere.api.model.Coordinates;
import org.mabartos.meetmethere.api.model.EventModel;
import org.mabartos.meetmethere.api.model.UserModel;
import org.mabartos.meetmethere.api.model.exception.ModelDuplicateException;
import org.mabartos.meetmethere.api.model.exception.ModelNotFoundException;
import org.mabartos.meetmethere.api.model.jpa.adapter.JpaAddressAdapter;
import org.mabartos.meetmethere.api.model.jpa.adapter.JpaEventAdapter;
import org.mabartos.meetmethere.api.model.jpa.adapter.JpaInvitationAdapter;
import org.mabartos.meetmethere.api.model.jpa.adapter.JpaUserAdapter;
import org.mabartos.meetmethere.api.model.jpa.entity.EventEntity;
import org.mabartos.meetmethere.api.provider.EventProvider;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mabartos.meetmethere.api.UpdateUtil.update;
import static org.mabartos.meetmethere.api.model.jpa.util.JpaUtil.catchNoResult;

@Transactional
public class JpaEventProvider implements EventProvider {

    private final MeetMeThereSession session;
    private final EntityManager em;

    public JpaEventProvider(MeetMeThereSession session, EntityManager em) {
        this.session = session;
        this.em = em;
    }

    @Override
    public EventModel getEventById(Long id) {
        return new JpaEventAdapter(session, em, EventEntity.findById(id));
    }

    @Override
    public Set<EventModel> searchByTitle(String title) {
        final Optional<List<EventEntity>> events = catchNoResult(() -> em.createQuery("select e from EventEntity e where e.title like :title", EventEntity.class)
                .setParameter("title", title)
                .getResultList()
        );

        if (events.isEmpty()) return Collections.emptySet();

        return convertToModelSet(events.get());
    }

    @Override
    public Set<EventModel> searchByCoordinates(Coordinates coordinates) {
        final Optional<List<EventEntity>> events = catchNoResult(() ->
                em.createQuery("select e from EventEntity e where e.venue.longitude=:longitude and e.venue.latitude =:latitude", EventEntity.class)
                        .setParameter("longitude", coordinates.getLongitude())
                        .setParameter("latitude", coordinates.getLatitude())
                        .getResultList()
        );

        if (events.isEmpty()) return Collections.emptySet();

        return convertToModelSet(events.get());
    }

    @Override
    public Set<EventModel> getEvents(int firstResult, int maxResults) {
        final Optional<List<EventEntity>> events = catchNoResult(() -> em.createQuery("select e from EventEntity e", EventEntity.class)
                .setFirstResult(firstResult)
                .setMaxResults(maxResults)
                .getResultList()
        );

        if (events.isEmpty()) return Collections.emptySet();

        return convertToModelSet(events.get());
    }

    @Override
    public long getEventsCount() {
        return EventEntity.count();
    }

    @Override
    public EventModel createEvent(EventModel event) throws ModelDuplicateException {
        if (JpaEventAdapter.convertToEntity(event, em) != null) {
            throw new ModelDuplicateException("Duplicate event");
        }

        EventEntity entity = convertEntity(event);
        em.persist(entity);
        em.flush();

        return new JpaEventAdapter(session, em, entity);
    }

    @Override
    public EventModel createEvent(String title, UserModel creator) {
        EventEntity entity = new EventEntity();
        entity.setTitle(title);
        entity.setCreator(JpaUserAdapter.convertToEntity(creator, em));

        em.persist(entity);
        em.flush();

        return new JpaEventAdapter(session, em, entity);
    }

    @Override
    public void removeEvent(Long id) throws ModelNotFoundException {
        if (!EventEntity.deleteById(id)) throw new ModelNotFoundException("Cannot find Event");
    }

    @Override
    public EventModel updateEvent(EventModel event) {
        EventEntity entity = Optional.ofNullable(JpaEventAdapter.convertToEntity(event, em))
                .orElseThrow(() -> new IllegalArgumentException("Cannot update Event"));

        convertEvent(entity, event);

        em.merge(entity);
        em.flush();

        return new JpaEventAdapter(session, em, entity);
    }

    private Set<EventModel> convertToModelSet(List<EventEntity> entities) {
        return entities.stream().map(f -> new JpaEventAdapter(session, em, f)).collect(Collectors.toSet());
    }

    public void convertEvent(EventEntity entity, EventModel model) {
        update(entity::setTitle, model::getEventTitle);
        update(entity::setDescription, model::getDescription);
        update(entity::setPublic, model::isPublic);
        update(entity::setCreatedAt, model::getCreatedAt);
        update(entity::setUpdatedAt, model::getUpdatedAt);
        update(entity::setStartTime, model::getStartTime);
        update(entity::setEndTime, model::getEndTime);
        update(entity::setAttributes, model::getAttributes);

        update(entity::setCreator, () -> JpaUserAdapter.convertToEntity(model.getCreatedBy(), em));
        update(entity::setUpdatedBy, () -> JpaUserAdapter.convertToEntity(model.getUpdatedBy(), em));
        update(entity::setVenue, () -> JpaAddressAdapter.convertToEntity(model.getVenue()));
        update(entity::setInvitations, () -> model.getInvitations()
                .stream()
                .map(f -> JpaInvitationAdapter.convertToEntity(f, em))
                .collect(Collectors.toSet()));
    }

    private EventEntity convertEntity(EventModel model) {
        EventEntity entity = new EventEntity();
        convertEvent(entity, model);
        return entity;
    }
}
