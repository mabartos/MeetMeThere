package org.mabartos.meetmethere.service.rest;

import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheResult;
import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;
import org.mabartos.meetmethere.interaction.rest.api.UserResource;
import org.mabartos.meetmethere.interaction.rest.api.model.UserJson;
import org.mabartos.meetmethere.interaction.rest.api.model.mapper.UserJsonDomainMapper;
import org.mapstruct.factory.Mappers;

import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.mabartos.meetmethere.api.model.eventbus.user.UserEventsNames.USER_GET_USER_EVENT;
import static org.mabartos.meetmethere.api.model.eventbus.user.UserEventsNames.USER_REMOVE_EVENT;
import static org.mabartos.meetmethere.api.model.eventbus.user.UserEventsNames.USER_UPDATE_EVENT;
import static org.mabartos.meetmethere.service.rest.UsersResourceProvider.getSingleUser;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Transactional
@Authenticated
public class UserResourceProvider implements UserResource {
    private static final UserJsonDomainMapper mapper = Mappers.getMapper(UserJsonDomainMapper.class);
    private final MeetMeThereSession session;
    private final String userId;

    public UserResourceProvider(MeetMeThereSession session, String userId) {
        this.session = session;
        this.userId = userId;
    }

    @GET
    @CacheResult(cacheName = UsersResourceProvider.CACHE_NAME)
    public Uni<UserJson> getUser() {
        return getSingleUser(session.eventBus(), USER_GET_USER_EVENT, userId);
    }

    @DELETE
    public Response removeUser() {
        session.eventBus().publish(USER_REMOVE_EVENT, userId);
        invalidateById(userId);
        return Response.ok().build();
    }

    @PATCH
    public Uni<UserJson> updateUser(UserJson user) {
        if (user.getId() != null && !user.getId().equals(userId)) {
            throw new BadRequestException("Cannot update Event - different IDs");
        }
        user.setId(userId);

        invalidateById(user.getId());
        invalidateByName(user.getUsername());
        invalidateByEmail(user.getEmail());

        return getSingleUser(session.eventBus(), USER_UPDATE_EVENT, mapper.toDomain(user));
    }

    @CacheInvalidate(cacheName = UsersResourceProvider.CACHE_NAME)
    void invalidateById(String id) {
    }

    @CacheInvalidate(cacheName = UsersResourceProvider.CACHE_NAME)
    void invalidateByName(String username) {
    }

    @CacheInvalidate(cacheName = UsersResourceProvider.CACHE_NAME)
    void invalidateByEmail(String email) {
    }
}
