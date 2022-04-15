package org.mabartos.meetmethere.model.jpa.provider;

import jakarta.persistence.EntityManager;
import org.mabartos.meetmethere.model.UserModel;
import org.mabartos.meetmethere.model.exception.ModelDuplicateException;
import org.mabartos.meetmethere.model.exception.ModelNotFoundException;
import org.mabartos.meetmethere.model.jpa.adapter.JpaUserAdapter;
import org.mabartos.meetmethere.model.jpa.entity.UserEntity;
import org.mabartos.meetmethere.model.provider.UserProvider;
import org.mabartos.meetmethere.session.MeetMeThereSession;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mabartos.meetmethere.UpdateUtil.update;

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
    public UserModel getUserByEmail(String email) {
        final UserEntity user = em.createQuery("select u from UserEntity u where u.email=:email", UserEntity.class)
                .setParameter("email", email)
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
    public UserModel createUser(String email, String username) throws ModelDuplicateException {
        if (getUserByEmail(email) != null || getUserByUsername(username) != null) {
            throw new ModelDuplicateException("Duplicate user");
        }

        UserEntity entity = new UserEntity();
        entity.setEmail(email);
        entity.setUsername(username);

        em.persist(entity);
        em.flush();

        return new JpaUserAdapter(session, em, entity);
    }

    @Override
    public UserModel createUser(UserModel user) throws ModelDuplicateException {
        if (UserEntity.findByIdOptional(user.getId()).isPresent()) {
            throw new ModelDuplicateException("Duplicate user");
        }

        UserEntity entity = convertEntity(user);
        em.persist(entity);
        em.flush();

        return new JpaUserAdapter(session, em, entity);
    }

    @Override
    public void removeUser(Long id) throws ModelNotFoundException {
        if (!UserEntity.deleteById(id)) throw new ModelNotFoundException("Cannot find User");
    }

    @Override
    public void updateUser(UserModel user) {
        UserEntity entity = UserEntity.<UserEntity>findByIdOptional(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Cannot update User"));

        convertUser(entity, user);

        em.merge(entity);
        em.flush();
    }

    private static void convertUser(UserEntity entity, UserModel model) {
        update(entity::setEmail, model::getEmail);
        update(entity::setUsername, model::getUsername);
        update(entity::setFirstName, model::getFirstName);
        update(entity::setLastName, model::getLastName);
        update(entity::setAttributes, model::getAttributes);
    }

    private static UserEntity convertEntity(UserModel model) {
        UserEntity entity = new UserEntity();
        convertUser(entity, model);
        return entity;
    }
}
