package com.dfsek.terra.addons.ore;

import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.world.generator.GenerationStageProvider;


public class OreAddon implements AddonInitializer {
    @Inject
    private Platform platform;
    
    @Inject
    private BaseAddon addon;
    
    @Override
    public void initialize() {
        platform.getEventManager()
                .getHandler(FunctionalEventHandler.class)
                .register(addon, ConfigPackPreLoadEvent.class)
                .then(event -> {
                    event.getPack().registerConfigType(new OreConfigType(), "ORE", 1);
                    event.getPack().getOrCreateRegistry(GenerationStageProvider.class).register("ORE", pack -> new OrePopulator(platform));
                })
                .failThrough();
    }
}
