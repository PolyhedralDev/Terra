package com.dfsek.terra.addons.biome.single;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.util.seeded.BiomeBuilder;

public class SingleBiomeProviderTemplate extends BiomeProviderTemplate {
    @Value("biome")
    private BiomeBuilder biome;

    public SingleBiomeProviderTemplate() {
    }

    @Override
    public BiomeProvider build(long seed) {
        return new SingleBiomeProvider(biome.apply(seed));
    }
}
