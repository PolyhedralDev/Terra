package com.dfsek.terra.api.core.event.events.world;

import com.dfsek.terra.api.core.event.events.PackEvent;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.world.TerraWorld;

/**
 * Called upon initialization of a TerraWorld.
 */
public class TerraWorldLoadEvent implements PackEvent {
    private final TerraWorld world;

    public TerraWorldLoadEvent(TerraWorld world) {
        this.world = world;
    }

    public TerraWorld getWorld() {
        return world;
    }

    @Override
    public ConfigPack getPack() {
        return world.getConfig();
    }
}
