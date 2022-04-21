package org.mabartos.meetmethere.service.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.mutiny.core.eventbus.Message;
import org.mabartos.meetmethere.api.domain.User;
import org.mabartos.meetmethere.api.model.UserModel;
import org.mabartos.meetmethere.api.model.eventbus.PaginationObject;
import org.mabartos.meetmethere.api.model.eventbus.UserModelSet;
import org.mabartos.meetmethere.api.service.UserService;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;
import org.mabartos.meetmethere.interaction.rest.api.UserResource;
import org.mabartos.meetmethere.interaction.rest.api.UsersResource;
import org.mabartos.meetmethere.interaction.rest.api.model.ModelToJson;
import org.mabartos.meetmethere.interaction.rest.api.model.UserJson;

import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.time.Duration;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mabartos.meetmethere.interaction.rest.api.ResourceConstants.FIRST_RESULT;
import static org.mabartos.meetmethere.interaction.rest.api.ResourceConstants.ID;
import static org.mabartos.meetmethere.interaction.rest.api.ResourceConstants.MAX_RESULTS;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
@Transactional
public class UsersResourceProvider implements UsersResource {
    private static final ObjectMapper mapper = new ObjectMapper();
    public static final Duration MAX_WAITING_TIME = Duration.ofSeconds(5);

    @Context
    MeetMeThereSession session;

    @GET
    @Path("/{id}")
    public UserResource getUserById(@PathParam(ID) Long id) {
        final UserModel user = session.eventBus()
                .<UserModel>request(UserService.USER_GET_USER_EVENT, id)
                .onItem()
                .transform(Message::body)
                .await()
                .atMost(MAX_WAITING_TIME);

        return new UserResourceProvider(session, user);
    }

    @GET
    @Path("/{username}")
    public UserResource getUserByUsername(@PathParam("username") String username) {
        final UserModel user = session.eventBus()
                .<UserModel>request(UserService.USER_GET_USERNAME_EVENT, username)
                .onItem()
                .transform(Message::body)
                .await()
                .atMost(MAX_WAITING_TIME);

        return new UserResourceProvider(session, user);
    }

    @Path("/email/{email}")
    public UserResource getUserByEmail(@PathParam("email") String email) {
        final UserModel user = session.eventBus()
                .<UserModel>request(UserService.USER_GET_EMAIL_EVENT, email)
                .onItem()
                .transform(Message::body)
                .await()
                .atMost(MAX_WAITING_TIME);

        return new UserResourceProvider(session, user);
    }

    @GET
    public Uni<Set<UserJson>> getUsers(@QueryParam(FIRST_RESULT) Integer firstResult,
                                       @QueryParam(MAX_RESULTS) Integer maxResults) {
        firstResult = firstResult != null ? firstResult : 0;
        maxResults = maxResults != null ? maxResults : Integer.MAX_VALUE;

        return getSetOfUsers(session.eventBus(), UserService.USER_GET_USERS_EVENT, new PaginationObject(firstResult, maxResults));
    }

    @GET
    @Path("/count")
    public Uni<Long> getUsersCount() {
        return Uni.createFrom().item(session.userStorage().getUsersCount());
    }

    @POST
    public void createUser(UserJson user) {
        User newUser = new User(user.getUsername(), user.getEmail());

        //Workaround for now, because there's no codec for inherited UserJson and User POJO
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setAttributes(user.getAttributes());

        session.eventBus().send(UserService.USER_CREATE_EVENT, newUser);

       /* return getSingleUser(session.eventBus(), UserService.USER_CREATE_EVENT, newUser)
                .onFailure(ModelDuplicateException.class)
                .transform(f -> new WebApplicationException(f.getMessage(), 409));*/
    }

    protected static Uni<UserJson> getSingleUser(EventBus bus, String address, Object object) {
        return bus.<UserModel>request(address, object).onItem().transform(Message::body).map(ModelToJson::toJson);
    }

    protected static Uni<Set<UserJson>> getSetOfUsers(EventBus bus, String address, Object object) {
        return bus.<UserModelSet>request(address, object)
                .onItem()
                .transform(Message::body)
                .map(f -> f.getSet()
                        .stream()
                        .filter(Objects::nonNull)
                        .map(ModelToJson::toJson)
                        .collect(Collectors.toSet())
                );
    }
}
