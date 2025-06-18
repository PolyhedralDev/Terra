package com.dfsek.terra.minestom.addon;

import ca.solostudios.strata.Versions;
import ca.solostudios.strata.version.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.event.events.config.ConfigurationLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.minestom.TerraMinestomPlatform;
import com.dfsek.terra.minestom.config.VanillaBiomeProperties;


public class MinestomAddon implements BaseAddon {
    private static final Version VERSION = Versions.getVersion(1, 0, 0);
    private static final Logger logger = LoggerFactory.getLogger(MinestomAddon.class);
    private final TerraMinestomPlatform minestomPlatform;

    public MinestomAddon(TerraMinestomPlatform minestomPlatform) {
        this.minestomPlatform = minestomPlatform;
    }

    @Override
    public void initialize() {
        minestomPlatform.getEventManager()
            .getHandler(FunctionalEventHandler.class)
            .register(this, ConfigurationLoadEvent.class)
            .then(event -> {
                if(event.is(Biome.class)) {
                    event.getLoadedObject(Biome.class).getContext().put(event.load(new VanillaBiomeProperties()));
                }
            })
            .global();
    }

    @Override
    public Version getVersion() { return VERSION; }

    @Override
    public String getID() { return "terra-minestom"; }
}
