package org.mabartos.meetmethere.service.rest;

import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import org.mabartos.meetmethere.api.domain.User;
import org.mabartos.meetmethere.api.model.eventbus.PaginationObject;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;
import org.mabartos.meetmethere.interaction.rest.api.UserResource;
import org.mabartos.meetmethere.interaction.rest.api.UsersResource;
import org.mabartos.meetmethere.interaction.rest.api.model.UserJson;
import org.mabartos.meetmethere.interaction.rest.api.model.mapper.UserJsonDomainMapper;
import org.mabartos.meetmethere.service.rest.util.EventBusUtil;
import org.mapstruct.factory.Mappers;

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
import java.util.Set;

import static org.mabartos.meetmethere.api.model.eventbus.user.UserEventsNames.USER_CREATE_EVENT;
import static org.mabartos.meetmethere.api.model.eventbus.user.UserEventsNames.USER_GET_EMAIL_EVENT;
import static org.mabartos.meetmethere.api.model.eventbus.user.UserEventsNames.USER_GET_USERNAME_EVENT;
import static org.mabartos.meetmethere.api.model.eventbus.user.UserEventsNames.USER_GET_USERS_EVENT;
import static org.mabartos.meetmethere.interaction.rest.api.ResourceConstants.FIRST_RESULT;
import static org.mabartos.meetmethere.interaction.rest.api.ResourceConstants.ID;
import static org.mabartos.meetmethere.interaction.rest.api.ResourceConstants.MAX_RESULTS;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
@Transactional
public class UsersResourceProvider implements UsersResource {
    private static final UserJsonDomainMapper mapper = Mappers.getMapper(UserJsonDomainMapper.class);
    static final String CACHE_NAME = "users-resource-provider-cache";

    @Context
    MeetMeThereSession session;

    @Path("/{id}")
    @CacheResult(cacheName = CACHE_NAME)
    public UserResource getUserById(@PathParam(ID) Long id) {
        return new UserResourceProvider(session, id);
    }

    @GET
    @Path("/username/{username}")
    @CacheResult(cacheName = CACHE_NAME)
    public Uni<UserJson> getUserByUsername(@PathParam("username") String username) {
        return getSingleUser(session.eventBus(), USER_GET_USERNAME_EVENT, username);
    }

    @GET
    @Path("/email/{email}")
    @CacheResult(cacheName = CACHE_NAME)
    public Uni<UserJson> getUserByEmail(@PathParam("email") String email) {
        return getSingleUser(session.eventBus(), USER_GET_EMAIL_EVENT, email);
    }

    @GET
    @CacheResult(cacheName = CACHE_NAME)
    public Uni<Set<UserJson>> getUsers(@QueryParam(FIRST_RESULT) Integer firstResult,
                                       @QueryParam(MAX_RESULTS) Integer maxResults) {
        firstResult = firstResult != null ? firstResult : 0;
        maxResults = maxResults != null ? maxResults : Integer.MAX_VALUE;

        return getSetOfUsers(session.eventBus(), USER_GET_USERS_EVENT, new PaginationObject(firstResult, maxResults));
    }

    @GET
    @Path("/count")
    @CacheResult(cacheName = CACHE_NAME)
    public Uni<Long> getUsersCount() {
        return Uni.createFrom().item(session.userStorage().getUsersCount());
    }

    @POST
    public Uni<Long> createUser(UserJson user) {
        return EventBusUtil.createEntity(session.eventBus(), USER_CREATE_EVENT, mapper.toDomain(user));
    }

    protected static Uni<UserJson> getSingleUser(EventBus bus, String address, Object object) {
        return EventBusUtil.<User>getSingleEntity(bus, address, object).map(mapper::toJson);
    }

    protected static Uni<Set<UserJson>> getSetOfUsers(EventBus bus, String address, Object object) {
        return EventBusUtil.getSetOfEntities(bus, address, object, mapper::toJson);
    }
}
