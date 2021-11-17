package com.dfsek.terra.addons.chunkgenerator;

import com.dfsek.terra.addons.chunkgenerator.generation.generators.NoiseChunkGenerator3D;
import com.dfsek.terra.addons.chunkgenerator.palette.PaletteHolder;
import com.dfsek.terra.addons.chunkgenerator.palette.PaletteHolderLoader;
import com.dfsek.terra.addons.chunkgenerator.palette.SlantHolder;
import com.dfsek.terra.addons.chunkgenerator.palette.SlantHolderLoader;
import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.event.events.config.ConfigurationLoadEvent;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.generator.ChunkGeneratorProvider;


public class NoiseChunkGenerator3DAddon implements AddonInitializer {
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
                    event.getPack().getOrCreateRegistry(ChunkGeneratorProvider.class).register("NOISE_3D",
                                                                                               pack -> new NoiseChunkGenerator3D(pack,
                                                                                                                                 platform));
                    event.getPack()
                         .applyLoader(SlantHolder.class, new SlantHolderLoader())
                         .applyLoader(PaletteHolder.class, new PaletteHolderLoader());
                })
                .failThrough();
        
        platform.getEventManager()
                .getHandler(FunctionalEventHandler.class)
                .register(addon, ConfigurationLoadEvent.class)
                .then(event -> {
                    if(event.is(TerraBiome.class)) {
                        event.getLoadedObject(TerraBiome.class).getContext().put(event.load(new BiomePaletteTemplate()).get());
                    }
                })
                .failThrough();
    }
}
