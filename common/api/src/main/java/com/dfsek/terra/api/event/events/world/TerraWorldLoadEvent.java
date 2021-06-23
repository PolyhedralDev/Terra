package com.dfsek.terra.api.event.events.world;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.event.events.PackEvent;


/**
 * Called upon initialization of a TerraWorld.
 */
public class TerraWorldLoadEvent implements PackEvent {
    private final TerraWorld world;
    private final ConfigPack pack;

    public TerraWorldLoadEvent(TerraWorld world, ConfigPack pack) {
        this.world = world;
        this.pack = pack;
    }

    public TerraWorld getWorld() {
        return world;
    }

    public ConfigPack getPack() {
        return pack;
    }

    public WorldConfig getWorldConfig() {
        return world.getConfig();
    }
}
