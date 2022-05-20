package org.mabartos.meetmethere.api.model.eventbus.event;

public interface EventEventsNames {
    String EVENT_CREATE_EVENT = "eventCreateEvent";
    String EVENT_UPDATE_EVENT = "eventUpdateEvent";
    String EVENT_REMOVE_EVENT = "eventRemoveEvent";
    String EVENT_GET_EVENTS_EVENT = "eventGetMultipleEvent";
    String EVENT_GET_EVENT_EVENT = "eventGetSingleEvent";
    String EVENT_SEARCH_TITLE_EVENT = "eventSearchByTitleEvent";
    String EVENT_SEARCH_COORDINATES_EVENT = "eventSearchByCoordinatesEvent";
    String EVENT_COUNT_EVENT = "eventCountEvent";
}
