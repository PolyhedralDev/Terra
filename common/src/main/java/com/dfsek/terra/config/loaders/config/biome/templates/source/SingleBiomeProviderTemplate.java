package com.dfsek.terra.config.loaders.config.biome.templates.source;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.biome.TerraBiome;
import com.dfsek.terra.biome.provider.BiomeProvider;
import com.dfsek.terra.biome.provider.SingleBiomeProvider;
import com.dfsek.terra.registry.BiomeRegistry;

public class SingleBiomeProviderTemplate extends BiomeProviderTemplate {
    @Value("biome")
    private TerraBiome biome;

    public SingleBiomeProviderTemplate(BiomeRegistry registry) {
        super(registry);
    }

    @Override
    public BiomeProvider build(long seed) {
        return new SingleBiomeProvider(biome);
    }
}
