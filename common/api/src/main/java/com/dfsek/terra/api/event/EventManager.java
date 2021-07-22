package com.dfsek.terra.api.event;

import com.dfsek.terra.api.event.events.Event;
import com.dfsek.terra.api.event.functional.EventContext;
import com.dfsek.terra.api.util.reflection.TypeKey;

/**
 * Manages event registration and triggering.
 */
public interface EventManager {
    /**
     * Call an event, and return the execution status.
     *
     * @param event Event to pass to all registered EventListeners.
     */
    void callEvent(Event event);

    <T extends EventHandler> void registerHandler(Class<T> clazz, T handler);

    <T extends EventHandler> T getHandler(Class<T> clazz);
}
