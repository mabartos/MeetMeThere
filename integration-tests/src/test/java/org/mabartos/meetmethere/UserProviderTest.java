package org.mabartos.meetmethere;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mabartos.meetmethere.api.model.UserModel;
import org.mabartos.meetmethere.api.model.exception.ModelDuplicateException;
import org.mabartos.meetmethere.api.model.exception.ModelNotFoundException;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;

import javax.inject.Inject;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@QuarkusTest
@TestTransaction
public class UserProviderTest {

    @Inject
    MeetMeThereSession session;

    @Test
    public void createUser() throws ModelDuplicateException {
        final UserModel model = assertCreateUser("email@test", "username");

        assertThat(session.users().getUsersCount(), is(1L));

        final UserModel foundById = session.users().getUserById(model.getId());
        assertThat(foundById, notNullValue());
        assertThat(foundById, is(model));
        final Set<UserModel> users = session.users().getUsers(0, Integer.MAX_VALUE);
        assertThat(users, notNullValue());
        assertThat(users.size(), is(1));

        final UserModel found = users.stream().findFirst().orElse(null);
        assertThat(found, notNullValue());
        assertThat(found, is(model));
    }

    @Test
    public void updateUser() throws ModelDuplicateException {
        UserModel model = assertCreateUser("email@test", "username");

        model.setFirstName("FirstName");
        model.setLastName("LastName");
        model.setAttribute("something", "exciting");
        UserModel updated = session.users().updateUser(model);

        assertThat(updated, notNullValue());
        assertThat(updated.getFirstName(), is("FirstName"));
        assertThat(updated.getLastName(), is("LastName"));

        final Map<String, String> attributes = updated.getAttributes();

        assertThat(attributes, notNullValue());
        assertThat(attributes.get("something"), is("exciting"));
    }

    @Test
    public void removeUser() throws ModelDuplicateException, ModelNotFoundException {
        final UserModel model = assertCreateUser("email@test", "username");

        assertThat(session.users().getUsersCount(), is(1L));

        session.users().removeUser(model.getId());

        assertThat(session.users().getUsersCount(), is(0L));
    }

    @Test
    public void removeInvalidUser() {
        try {
            session.users().removeUser(-4L);
            Assertions.fail();
        } catch (ModelNotFoundException expected) {
        }
    }

    private UserModel assertCreateUser(String email, String username) throws ModelDuplicateException {
        UserModel model = session.users().createUser(email, username);
        assertThat(model, notNullValue());
        assertThat(model.getEmail(), is(email));
        assertThat(model.getUsername(), is(username));
        assertThat(model.getId(), notNullValue());
        return model;
    }

}
