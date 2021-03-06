package org.mabartos.meetmethere;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.mabartos.meetmethere.api.domain.User;
import org.mabartos.meetmethere.api.mapper.ModelToDomain;
import org.mabartos.meetmethere.api.model.UserModel;
import org.mabartos.meetmethere.api.model.exception.ModelDuplicateException;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;
import org.mabartos.meetmethere.interaction.rest.api.model.AddressJson;
import org.mabartos.meetmethere.interaction.rest.api.model.EventJson;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@QuarkusTest
@TestTransaction
public class EventsResourceTest {

    private static final ObjectMapper mapper = new ObjectMapper();

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
    public void createEvent() throws ModelDuplicateException, JsonProcessingException {
        User dto = ModelToDomain.toDomain(session.userStorage().createUser("email@test", "username"));
        assertThat(dto, notNullValue());

        final UserModel found = session.userStorage().getUserById(dto.getId());
        assertThat(found, notNullValue());
        assertThat(found.getUsername(), is("username"));
        assertThat(found.getEmail(), is("email@test"));

        EventJson event = new EventJson("New Event", dto.getId(), String.format("%s %s", dto.getFirstName(), dto.getLastName()), new AddressJson("Czech Republic"));
        given()
                .when()
                .body(mapper.writeValueAsString(event))
                .contentType(MediaType.APPLICATION_JSON)
                .post("/events")
                .then()
                .statusCode(200);

    }

    private void assertEvents(EventJson... events) {
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