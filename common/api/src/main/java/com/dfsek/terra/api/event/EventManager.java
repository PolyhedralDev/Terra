package com.dfsek.terra.api.event;

import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.event.events.Event;

/**
 * Manages event registration and triggering.
 */
public interface EventManager {
    /**
     * Call an event, and return the execution status.
     *
     * @param event Event to pass to all registered EventListeners.
     * @return False if the event is cancellable and has been cancelled, otherwise true.
     */
    boolean callEvent(Event event);

    /**
     * Register an {@link EventListener} under an {@link TerraAddon}.
     *
     * @param addon    Addon to register listener for.
     * @param listener Listener to register.
     */
    void registerListener(TerraAddon addon, EventListener listener);
}
