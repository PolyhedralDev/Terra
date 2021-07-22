package com.dfsek.terra.api.event.functional;

import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.event.EventHandler;
import com.dfsek.terra.api.event.events.Event;
import com.dfsek.terra.api.util.reflection.TypeKey;

public interface FunctionalEventHandler extends EventHandler {
    <T extends Event> EventContext<T> register(TerraAddon addon, Class<T> clazz);

    <T extends Event> EventContext<T> register(TerraAddon addon, TypeKey<T> clazz);
}
