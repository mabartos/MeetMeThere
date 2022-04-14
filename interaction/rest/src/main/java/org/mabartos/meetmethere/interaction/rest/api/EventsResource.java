package org.mabartos.meetmethere.interaction.rest.api;

import org.mabartos.meetmethere.interaction.rest.api.dto.Event;
import org.mabartos.meetmethere.model.Coordinates;

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
public interface EventsResource {

    @GET
    Set<Event> getEvents(@QueryParam(FIRST_RESULT) int firstResult, @QueryParam(MAX_RESULTS) int maxResults);

    @GET
    @Path("/{id}")
    EventResource getEventById(@PathParam(ID) Long id);

    @GET
    @Path("/{title}")
    Set<Event> searchEventsByTitle(@PathParam("title") String title);

    @GET
    @Path("/coordinates")
    Set<Event> searchEventsByCoordinates(Coordinates coordinates);

    @POST
    Event createEvent(Event event);

}
