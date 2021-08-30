package com.dfsek.terra.sponge;

import org.spongepowered.api.Server;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.StartingEngineEvent;

import com.dfsek.terra.api.event.events.platform.PlatformInitializationEvent;


public class SpongeListener {
    private final TerraSpongePlugin plugin;
    
    public SpongeListener(TerraSpongePlugin plugin) {
        this.plugin = plugin;
    }
    
    @Listener
    public void initialize(StartingEngineEvent<Server> event) {
        plugin.getTerraPlugin().getEventManager().callEvent(new PlatformInitializationEvent());
    }
}
