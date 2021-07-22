package com.dfsek.terra.addons.chunkgenerator;

import com.dfsek.terra.addons.chunkgenerator.generation.generators.NoiseChunkGenerator3D;
import com.dfsek.terra.addons.chunkgenerator.palette.PaletteHolder;
import com.dfsek.terra.addons.chunkgenerator.palette.PaletteHolderLoader;
import com.dfsek.terra.addons.chunkgenerator.palette.SlantHolder;
import com.dfsek.terra.addons.chunkgenerator.palette.SlantHolderLoader;
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
import com.dfsek.terra.api.world.generator.ChunkGeneratorProvider;

@Addon("chunk-generator-noise-3d")
@Author("Terra")
@Version("1.0.0")
public class NoiseChunkGenerator3DAddon extends TerraAddon {
    @Inject
    private TerraPlugin main;

    @Override
    public void initialize() {
        main.getEventManager()
                .getHandler(FunctionalEventHandler.class)
                .register(this, ConfigPackPreLoadEvent.class)
                .then(event -> {
                    event.getPack().getOrCreateRegistry(ChunkGeneratorProvider.class).register("NOISE_3D", pack -> new NoiseChunkGenerator3D(pack, main));
                    event.getPack()
                            .applyLoader(SlantHolder.class, new SlantHolderLoader())
                            .applyLoader(PaletteHolder.class, new PaletteHolderLoader());
                })
                .failThrough();

        main.getEventManager()
                .getHandler(FunctionalEventHandler.class)
                .register(this, ConfigurationLoadEvent.class)
                .then(event -> {
                    if(event.is(TerraBiome.class)) {
                        event.getLoadedObject(TerraBiome.class).getContext().put(event.load(new BiomePaletteTemplate()).get());
                    }
                });
    }
}
