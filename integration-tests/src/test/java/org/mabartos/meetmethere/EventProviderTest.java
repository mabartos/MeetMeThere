package org.mabartos.meetmethere;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mabartos.meetmethere.api.model.EventModel;
import org.mabartos.meetmethere.api.model.UserModel;
import org.mabartos.meetmethere.api.model.exception.ModelDuplicateException;
import org.mabartos.meetmethere.api.model.exception.ModelNotFoundException;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@QuarkusTest
@TestTransaction
public class EventProviderTest {

    @Inject
    MeetMeThereSession session;

    @Test
    public void createEvent() throws ModelDuplicateException {
        final EventModel model = assertCreateEvent("New Event");

        assertThat(session.eventsStorage().getEventsCount(), is(1L));

        final EventModel foundById = session.eventsStorage().getEventById(model.getId());
        assertThat(foundById, notNullValue());
        assertThat(foundById, is(model));
        final Set<EventModel> events = session.eventsStorage().getEvents(0, Integer.MAX_VALUE);
        assertThat(events, notNullValue());
        assertThat(events.size(), is(1));

        final EventModel found = events.stream().findFirst().orElse(null);
        assertThat(found, notNullValue());
        assertThat(found, is(model));
    }

    @Test
    public void updateEvent() throws ModelDuplicateException {
        final EventModel model = assertCreateEvent("New Event");

        model.setDescription("Description");
        model.setStartTime(LocalDateTime.of(2022, 4, 17, 20, 0));
        model.setEndTime(LocalDateTime.of(2022, 4, 17, 22, 0));
        model.setAttribute("something", "exciting");
        EventModel updated = session.eventsStorage().updateEvent(model);

        assertThat(updated, notNullValue());
        assertThat(updated.getStartTime(), is(LocalDateTime.of(2022, 4, 17, 20, 0)));
        assertThat(updated.getEndTime(), is(LocalDateTime.of(2022, 4, 17, 22, 0)));

        final Map<String, String> attributes = updated.getAttributes();

        assertThat(attributes, notNullValue());
        assertThat(attributes.get("something"), is("exciting"));
    }

    @Test
    public void removeEvent() throws ModelNotFoundException, ModelDuplicateException {
        final EventModel model = assertCreateEvent("New Event");

        assertThat(session.eventsStorage().getEventsCount(), is(1L));

        session.eventsStorage().removeEvent(model.getId());

        assertThat(session.eventsStorage().getEventsCount(), is(0L));
    }

    @Test
    public void removeInvalidEvent() {
        try {
            session.eventsStorage().removeEvent(-4L);
            Assertions.fail();
        } catch (ModelNotFoundException expected) {
        }
    }

    private EventModel assertCreateEvent(String title) throws ModelDuplicateException {
        UserModel creator = session.users().createUser("creator@mail", "creator123");
        assertThat(creator, notNullValue());

        EventModel model = session.eventsStorage().createEvent(title, creator);
        assertThat(model, notNullValue());
        assertThat(model.getEventTitle(), is(title));
        assertThat(model.getId(), notNullValue());
        return model;
    }


}
