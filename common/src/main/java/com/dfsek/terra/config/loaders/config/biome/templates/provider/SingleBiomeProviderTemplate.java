package com.dfsek.terra.config.loaders.config.biome.templates.provider;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.biome.provider.BiomeProvider;
import com.dfsek.terra.api.world.biome.provider.SingleBiomeProvider;

public class SingleBiomeProviderTemplate extends BiomeProviderTemplate {
    @Value("biome")
    private TerraBiome biome;

    public SingleBiomeProviderTemplate() {
    }

    @Override
    public BiomeProvider build(long seed) {
        return new SingleBiomeProvider(biome);
    }
}
