package org.mabartos.meetmethere.mapper;

import org.mabartos.meetmethere.dto.Event;
import org.mabartos.meetmethere.model.EventModel;

public class EventMapper implements ModelDtoMapper<Event, EventModel> {
    @Override
    public EventModel toModel(Event event, EventModel eventModel) {
        return null;
    }

    @Override
    public Event toDto(EventModel eventModel) {
        return null;
    }
}
