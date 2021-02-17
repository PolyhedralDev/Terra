package com.dfsek.terra.api.core.event;

import com.dfsek.terra.api.core.event.events.Event;

public interface EventManager {
    /**
     * Call an event, and return the execution status.
     * @param event Event to pass to all registered EventListeners.
     * @return False if the event is cancellable and has been cancelled, otherwise true.
     */
    boolean callEvent(Event event);

    void registerListener(EventListener listener);
}
