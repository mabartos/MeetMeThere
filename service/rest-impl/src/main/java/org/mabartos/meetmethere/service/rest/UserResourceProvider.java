package org.mabartos.meetmethere.service.rest;

import io.smallrye.mutiny.Uni;
import org.mabartos.meetmethere.api.service.UserService;
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

import static org.mabartos.meetmethere.service.rest.UsersResourceProvider.getSingleUser;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class UserResourceProvider implements UserResource {
    private static final UserJsonDomainMapper mapper = Mappers.getMapper(UserJsonDomainMapper.class);
    private final MeetMeThereSession session;
    private final Long userId;

    public UserResourceProvider(MeetMeThereSession session, Long userId) {
        this.session = session;
        this.userId = userId;
    }

    @GET
    public Uni<UserJson> getUser() {
        return getSingleUser(session.eventBus(), UserService.USER_GET_USER_EVENT, userId);
    }

    @DELETE
    public Response removeUser() {
        session.eventBus().publish(UserService.USER_REMOVE_EVENT, userId);
        return Response.ok().build();
    }

    @PATCH
    public Uni<UserJson> updateUser(UserJson user) {
        if (user.getId() != null && !user.getId().equals(userId)) {
            throw new BadRequestException("Cannot update Event - different IDs");
        }
        user.setId(userId);

        return getSingleUser(session.eventBus(), UserService.USER_UPDATE_EVENT, mapper.toDomain(user));
    }
}
