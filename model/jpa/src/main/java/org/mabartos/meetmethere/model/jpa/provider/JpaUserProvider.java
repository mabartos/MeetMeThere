package org.mabartos.meetmethere.model.jpa.provider;

import jakarta.persistence.EntityManager;
import org.mabartos.meetmethere.model.UserModel;
import org.mabartos.meetmethere.model.jpa.adapter.JpaUserAdapter;
import org.mabartos.meetmethere.model.jpa.entity.UserEntity;
import org.mabartos.meetmethere.model.provider.UserProvider;
import org.mabartos.meetmethere.session.MeetMeThereSession;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class JpaUserProvider implements UserProvider {
    private final MeetMeThereSession session;
    private final EntityManager em;

    public JpaUserProvider(MeetMeThereSession session, EntityManager em) {
        this.session = session;
        this.em = em;
    }

    @Override
    public UserModel getUserById(Long id) {
        return new JpaUserAdapter(session, em, em.find(UserEntity.class, id));
    }

    @Override
    public UserModel getUserByUsername(String username) {
        final UserEntity user = em.createQuery("select u from UserEntity u where u.username=:username", UserEntity.class)
                .setParameter("username", username)
                .getSingleResult();

        return new JpaUserAdapter(session, em, user);
    }

    @Override
    public Set<UserModel> getUsers(int firstResult, int maxResults) {
        final List<UserEntity> users = em.createQuery("select u from UserEntity u", UserEntity.class)
                .setFirstResult(firstResult)
                .setMaxResults(maxResults)
                .getResultList();

        return users.stream()
                .filter(Objects::nonNull)
                .map(f -> new JpaUserAdapter(session, em, f))
                .collect(Collectors.toSet());
    }

    @Override
    public long getUsersCount() {
        return UserEntity.count();
    }

    @Override
    public void createUser(UserModel user) {
        UserEntity entity = convertUser(user);

        //TODO add attributes,..
        em.persist(entity);
        em.flush();
    }

    @Override
    public void removeUser(Long id) {
        UserEntity.deleteById(id);
    }

    @Override
    public void updateUser(Long id, UserModel user) {
        //TODO
    }

    private UserEntity convertUser(UserModel model) {
        UserEntity entity = new UserEntity();
        entity.setEmail(model.getEmail());
        entity.setUsername(model.getUsername());
        entity.setFirstName(model.getFirstName());
        entity.setLastName(model.getLastName());
        return entity;
    }
}
