package com.dfsek.terra.sponge;

import com.dfsek.terra.api.event.events.platform.PlatformInitializationEvent;
import org.spongepowered.api.Server;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.StartingEngineEvent;

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
