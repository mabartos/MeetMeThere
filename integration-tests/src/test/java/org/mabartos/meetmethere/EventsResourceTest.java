package org.mabartos.meetmethere;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.mabartos.meetmethere.api.domain.Event;
import org.mabartos.meetmethere.api.domain.User;
import org.mabartos.meetmethere.api.model.UserModel;
import org.mabartos.meetmethere.api.model.exception.ModelDuplicateException;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;
import org.mabartos.meetmethere.interaction.rest.api.model.ModelToJson;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mabartos.meetmethere.interaction.rest.api.model.ModelToJson.toJson;

@QuarkusTest
@TestTransaction
public class EventsResourceTest {

    @Inject
    MeetMeThereSession session;

    @Test
    public void getAll() {
        assertEvents();
    }

    @Test
    public void count() {
        assertCount(0);
    }

    @Test
    public void createEvent() throws ModelDuplicateException {
        User dto = ModelToJson.toJson(session.users().createUser("email@test", "username"));
        assertThat(dto, notNullValue());

        final UserModel found = session.users().getUserById(dto.getId());
        assertThat(found, notNullValue());
        assertThat(found.getUsername(), is("username"));
        assertThat(found.getEmail(), is("email@test"));

        Event event = new Event("New Event", dto);
        given()
                .when()
                .body(event)
                .contentType(MediaType.APPLICATION_JSON)
                .post("/events")
                .then()
                .statusCode(200);

    }

    private void assertEvents(Event... events) {
        given()
                .when().get("/events")
                .then()
                .statusCode(200)
                .body(is(events.length == 0 ? "[]" : events));
    }

    private void assertCount(Integer count) {
        given()
                .when().get("/events/count")
                .then()
                .statusCode(200)
                .body(is(count.toString()));
    }

}