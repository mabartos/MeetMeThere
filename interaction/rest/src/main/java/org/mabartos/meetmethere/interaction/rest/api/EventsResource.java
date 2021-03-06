package org.mabartos.meetmethere.interaction.rest.api;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.mabartos.meetmethere.interaction.rest.api.model.EventJson;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Set;

import static org.mabartos.meetmethere.interaction.rest.api.ResourceConstants.FIRST_RESULT;
import static org.mabartos.meetmethere.interaction.rest.api.ResourceConstants.ID;
import static org.mabartos.meetmethere.interaction.rest.api.ResourceConstants.MAX_RESULTS;

@Path("/events")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Transactional
@Tag(name = "Events Resource API", description = "Provide API for all events.")
public interface EventsResource {

    @GET
    Uni<Set<EventJson>> getEvents(@QueryParam(FIRST_RESULT) Integer firstResult,
                                  @QueryParam(MAX_RESULTS) Integer maxResults);

    @Path("/{id}")
    EventResource getEventById(@PathParam(ID) Long id);

    @GET
    @Path("/title/{title}")
    Uni<Set<EventJson>> searchEventsByTitle(@PathParam("title") String title);

    @GET
    @Path("/coordinates")
    Uni<Set<EventJson>> searchEventsByCoordinates(@QueryParam("longitude") Double longitude,
                                                  @QueryParam("latitude") Double latitude);

    @POST
    Uni<Long> createEvent(EventJson event);

    @GET
    @Path("/count")
    Uni<Long> getEventsCount();

}
