/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.event.functional;

import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.event.EventHandler;
import com.dfsek.terra.api.event.events.Event;
import com.dfsek.terra.api.util.reflection.TypeKey;


public interface FunctionalEventHandler extends EventHandler {
    <T extends Event> EventContext<T> register(BaseAddon addon, Class<T> clazz);
    
    <T extends Event> EventContext<T> register(BaseAddon addon, TypeKey<T> clazz);
}
