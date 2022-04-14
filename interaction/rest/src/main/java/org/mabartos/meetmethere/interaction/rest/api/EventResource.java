package org.mabartos.meetmethere.interaction.rest.api;

import org.mabartos.meetmethere.interaction.rest.api.dto.Event;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface EventResource {

    @GET
    Event getEvent();

    @DELETE
    void removeEvent();

    @PATCH
    void updateEvent(Event event);
}
