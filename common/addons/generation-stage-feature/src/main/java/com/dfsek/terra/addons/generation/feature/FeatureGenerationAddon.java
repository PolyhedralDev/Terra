package com.dfsek.terra.addons.generation.feature;

import com.dfsek.terra.addons.generation.feature.config.BiomeFeaturesTemplate;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.addon.annotations.Addon;
import com.dfsek.terra.api.addon.annotations.Author;
import com.dfsek.terra.api.addon.annotations.Version;
import com.dfsek.terra.api.event.events.config.ConfigurationLoadEvent;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.generator.GenerationStageProvider;


@Addon("generation-stage-feature")
@Version("1.0.0")
@Author("Terra")
public class FeatureGenerationAddon extends TerraAddon {
    @Inject
    private Platform platform;
    
    @Override
    public void initialize() {
        platform.getEventManager()
                .getHandler(FunctionalEventHandler.class)
                .register(this, ConfigPackPreLoadEvent.class)
                .then(event -> event.getPack()
                                .getOrCreateRegistry(GenerationStageProvider.class)
                                .register("FEATURE", pack -> new FeatureGenerationStage(platform)))
                .failThrough();
        
        platform.getEventManager()
                .getHandler(FunctionalEventHandler.class)
                .register(this, ConfigurationLoadEvent.class)
                .then(event -> {
                if(event.is(TerraBiome.class)) {
                    event.getLoadedObject(TerraBiome.class).getContext().put(event.load(new BiomeFeaturesTemplate()).get());
                }
            })
                .failThrough();
    }
}
