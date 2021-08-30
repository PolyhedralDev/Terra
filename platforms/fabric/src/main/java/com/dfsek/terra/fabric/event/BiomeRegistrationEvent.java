package com.dfsek.terra.fabric.event;

import net.minecraft.util.registry.DynamicRegistryManager;

import com.dfsek.terra.api.event.events.Event;


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
