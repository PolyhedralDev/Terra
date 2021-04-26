package com.dfsek.terra.config.loaders.config.biome.templates.provider;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.world.biome.provider.BiomeProvider;
import com.dfsek.terra.api.world.biome.provider.SingleBiomeProvider;
import com.dfsek.terra.config.builder.BiomeBuilder;

/**
 * Configures a single-biome provider.
 */
public class SingleBiomeProviderTemplate extends BiomeProviderTemplate {
    /**
     * The biome.
     */
    @Value("biome")
    private BiomeBuilder biome;

    public SingleBiomeProviderTemplate() {
    }

    @Override
    public BiomeProvider build(long seed) {
        return new SingleBiomeProvider(biome.apply(seed));
    }
}
