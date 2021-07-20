package com.dfsek.terra.addons.biome.single;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.util.seeded.SeededBuilder;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;

public class SingleBiomeProviderTemplate implements ObjectTemplate<SeededBuilder<BiomeProvider>>, SeededBuilder<BiomeProvider> {
    @Value("biome")
    private TerraBiome biome;

    @Override
    public BiomeProvider build(long seed) {
        return new SingleBiomeProvider(biome);
    }

    @Override
    public SeededBuilder<BiomeProvider> get() {
        return this;
    }
}
