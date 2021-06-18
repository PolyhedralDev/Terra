package com.dfsek.terra.fabric.event;

import com.dfsek.terra.api.event.events.Event;
import net.minecraft.util.registry.DynamicRegistryManager;

/**
 * Fired when biomes should be registered.
 */
public class BiomeRegistrationEvent implements Event {
    private final DynamicRegistryManager registryManager;

    public BiomeRegistrationEvent(DynamicRegistryManager registryManager) {
        this.registryManager = registryManager;
    }

    public DynamicRegistryManager getRegistryManager() {
        return registryManager;
    }
}
