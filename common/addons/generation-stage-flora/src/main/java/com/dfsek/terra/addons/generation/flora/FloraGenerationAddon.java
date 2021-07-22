package com.dfsek.terra.addons.generation.flora;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.addon.annotations.Addon;
import com.dfsek.terra.api.addon.annotations.Author;
import com.dfsek.terra.api.addon.annotations.Version;
import com.dfsek.terra.api.event.events.config.ConfigurationLoadEvent;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.generator.GenerationStageProvider;

@Addon("generation-stage-flora")
@Version("1.0.0")
@Author("Terra")
public class FloraGenerationAddon extends TerraAddon {

    @Inject
    private TerraPlugin main;

    @Override
    public void initialize() {
        main.getEventManager()
                .getHandler(FunctionalEventHandler.class)
                .register(this, ConfigPackPreLoadEvent.class)
                .then(event -> {
                    event.getPack().applyLoader(FloraLayer.class, FloraLayerLoader::new);
                    event.getPack().getOrCreateRegistry(GenerationStageProvider.class).register("FLORA", pack -> new FloraGenerationStage(main));
                });

        main.getEventManager()
                .getHandler(FunctionalEventHandler.class)
                .register(this, ConfigurationLoadEvent.class)
                .then(event -> {
                    if(event.is(TerraBiome.class)) {
                        event.getLoadedObject(TerraBiome.class).getContext().put(event.load(new BiomeFloraTemplate()).get());
                    }
                });
    }
}
