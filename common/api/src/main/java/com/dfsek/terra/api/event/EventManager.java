/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.event;

import com.dfsek.terra.api.event.events.Event;


/**
 * Manages event registration and triggering.
 */
public interface EventManager {
    /**
     * Call an event, and return the execution status.
     *
     * @param event Event to pass to all registered EventListeners.
     */
    <T extends Event> T callEvent(T event);
    
    <T extends EventHandler> void registerHandler(Class<T> clazz, T handler);
    
    <T extends EventHandler> T getHandler(Class<T> clazz);
}
