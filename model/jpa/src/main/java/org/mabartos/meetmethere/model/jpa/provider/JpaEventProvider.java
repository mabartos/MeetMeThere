package org.mabartos.meetmethere.model.jpa.provider;

import javax.persistence.EntityManager;

import org.mabartos.meetmethere.model.Coordinates;
import org.mabartos.meetmethere.model.EventModel;
import org.mabartos.meetmethere.model.exception.ModelDuplicateException;
import org.mabartos.meetmethere.model.exception.ModelNotFoundException;
import org.mabartos.meetmethere.model.jpa.adapter.JpaEventAdapter;
import org.mabartos.meetmethere.model.jpa.entity.EventEntity;
import org.mabartos.meetmethere.model.provider.EventProvider;
import org.mabartos.meetmethere.session.MeetMeThereSession;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mabartos.meetmethere.UpdateUtil.update;

public class JpaEventProvider implements EventProvider {

    private final MeetMeThereSession session;
    private final EntityManager em;

    public JpaEventProvider(MeetMeThereSession session, EntityManager em) {
        this.session = session;
        this.em = em;
    }

    @Override
    public EventModel getEventById(Long id) {
        return EventEntity.findById(id);
    }

    @Override
    public Set<EventModel> searchByTitle(String title) {
        final List<EventEntity> events = em.createQuery("select e from EventEntity e where e.title like :title", EventEntity.class)
                .setParameter("title", title)
                .getResultList();

        return convertToModelSet(events);
    }

    @Override
    public Set<EventModel> searchByCoordinates(Coordinates coordinates) {
        final List<EventEntity> events = em.createQuery("select e from EventEntity e where e.venue.longitude=:longitude and e.venue.latitude =:latitude", EventEntity.class)
                .setParameter("longitude", coordinates.getLongitude())
                .setParameter("latitude", coordinates.getLatitude())
                .getResultList();

        return convertToModelSet(events);
    }

    @Override
    public Set<EventModel> getEvents(int firstResult, int maxResults) {
        final List<EventEntity> events = em.createQuery("select e from EventEntity e", EventEntity.class)
                .setFirstResult(firstResult)
                .setMaxResults(maxResults)
                .getResultList();

        return convertToModelSet(events);
    }

    @Override
    public long getEventsCount() {
        return EventEntity.count();
    }

    @Override
    public EventModel createEvent(EventModel event) throws ModelDuplicateException {
        if (EventEntity.findByIdOptional(event.getId()).isPresent()) {
            throw new ModelDuplicateException("Duplicate event");
        }

        EventEntity entity = convertEntity(event);
        em.persist(entity);
        em.flush();

        return new JpaEventAdapter(session, em, entity);
    }

    @Override
    public EventModel createEvent(String title) {
        EventEntity entity = new EventEntity();
        entity.setTitle(title);

        em.persist(entity);
        em.flush();

        return new JpaEventAdapter(session, em, entity);
    }

    @Override
    public void removeEvent(Long id) throws ModelNotFoundException {
        if (!EventEntity.deleteById(id)) throw new ModelNotFoundException("Cannot find Event");
    }

    @Override
    public void updateEvent(EventModel event) {
        EventEntity entity = EventEntity.<EventEntity>findByIdOptional(event.getId())
                .orElseThrow(() -> new IllegalArgumentException("Cannot update Event"));

        convertEvent(entity, event);

        em.merge(entity);
        em.flush();
    }

    private Set<EventModel> convertToModelSet(List<EventEntity> entities) {
        return entities.stream().map(f -> new JpaEventAdapter(session, em, f)).collect(Collectors.toSet());
    }

    public static void convertEvent(EventEntity entity, EventModel model) {
        update(entity::setTitle, model::getEventTitle);
        update(entity::setDescription, model::getDescription);
        update(entity::setPublic, model::isPublic);
        update(entity::setCreatedAt, model::getCreatedAt);
        update(entity::setUpdatedAt, model::getUpdatedAt);
        update(entity::setStartTime, model::getStartTime);
        update(entity::setEndTime, model::getEndTime);
        update(entity::setAttributes, model::getAttributes);

        //TODO
        /*update(entity::setInvitations, model::getAttributes);
        update(entity::setVenue, model::getVenue);
        update(entity::setUpdatedBy, model::getUpdatedBy);
        update(entity::setCreator, model::getCreatedBy);*/
    }

    private static EventEntity convertEntity(EventModel model) {
        EventEntity entity = new EventEntity();
        convertEvent(entity, model);
        return entity;
    }
}
