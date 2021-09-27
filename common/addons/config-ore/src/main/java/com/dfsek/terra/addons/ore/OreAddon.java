package com.dfsek.terra.addons.ore;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.addon.annotations.Addon;
import com.dfsek.terra.api.addon.annotations.Author;
import com.dfsek.terra.api.addon.annotations.Version;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.world.generator.GenerationStageProvider;


@Addon("config-ore")
@Author("Terra")
@Version("1.0.0")
public class OreAddon extends TerraAddon {
    @Inject
    private Platform platform;
    
    @Override
    public void initialize() {
        platform.getEventManager()
                .getHandler(FunctionalEventHandler.class)
                .register(this, ConfigPackPreLoadEvent.class)
                .then(event -> {
                event.getPack().registerConfigType(new OreConfigType(), "ORE", 1);
                event.getPack().getOrCreateRegistry(GenerationStageProvider.class).register("ORE", pack -> new OrePopulator(platform));
            })
                .failThrough();
    }
}
