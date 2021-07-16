package com.dfsek.terra.addons.chunkgenerator;

import com.dfsek.terra.addons.chunkgenerator.generation.generators.NoiseChunkGenerator3D;
import com.dfsek.terra.addons.chunkgenerator.palette.PaletteHolder;
import com.dfsek.terra.addons.chunkgenerator.palette.PaletteHolderLoader;
import com.dfsek.terra.addons.chunkgenerator.palette.PaletteInfo;
import com.dfsek.terra.addons.chunkgenerator.palette.SlantHolder;
import com.dfsek.terra.addons.chunkgenerator.palette.SlantHolderLoader;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.addon.annotations.Addon;
import com.dfsek.terra.api.addon.annotations.Author;
import com.dfsek.terra.api.addon.annotations.Version;
import com.dfsek.terra.api.event.EventListener;
import com.dfsek.terra.api.event.events.config.ConfigurationLoadEvent;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.registry.exception.DuplicateEntryException;
import com.dfsek.terra.api.util.seeded.BiomeBuilder;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.generator.ChunkGeneratorProvider;

import java.util.HashMap;
import java.util.Map;

@Addon("chunk-generator-noise-3d")
@Author("Terra")
@Version("1.0.0")
public class NoiseChunkGenerator3DAddon extends TerraAddon implements EventListener {
    @Inject
    private TerraPlugin main;

    private final Map<String, PaletteInfo> palettes = new HashMap<>();

    @Override
    public void initialize() {
        main.getEventManager().registerListener(this, this);
    }

    public void onPackLoad(ConfigPackPreLoadEvent event) throws DuplicateEntryException {
        event.getPack().getOrCreateRegistry(ChunkGeneratorProvider.class).register("NOISE_3D", pack -> new NoiseChunkGenerator3D(pack, main, this));
        event.getPack().applyLoader(SlantHolder.class, new SlantHolderLoader())
                .applyLoader(PaletteHolder.class, new PaletteHolderLoader());
    }

    public void onBiomeLoad(ConfigurationLoadEvent event) {
        if(event.is(BiomeBuilder.class)) {
            palettes.put(event.getConfiguration().getID(), event.load(new BiomePaletteTemplate()).get());
        }
    }

    public PaletteInfo getPalette(TerraBiome biome) {
        return palettes.get(biome.getID());
    }
}
