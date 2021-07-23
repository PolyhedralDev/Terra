package com.dfsek.terra.addons.biome.pipeline.config;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.addons.biome.pipeline.source.NoiseSource;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.addons.biome.pipeline.source.BiomeSource;

public class NoiseSourceTemplate extends SourceTemplate {
    @Value("noise")
    private @Meta NoiseSampler noise;

    @Value("biomes")
    private @Meta ProbabilityCollection<@Meta TerraBiome> biomes;

    @Override
    public BiomeSource get() {
        return new NoiseSource(biomes, noise);
    }
}
