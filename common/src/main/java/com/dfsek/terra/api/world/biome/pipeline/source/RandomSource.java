package com.dfsek.terra.api.world.biome.pipeline.source;

import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.util.collections.ProbabilityCollection;
import com.dfsek.terra.api.world.biome.TerraBiome;

public class RandomSource implements BiomeSource {
    private final ProbabilityCollection<TerraBiome> biomes;
    private final NoiseSampler sampler;

    public RandomSource(ProbabilityCollection<TerraBiome> biomes, NoiseSampler sampler) {
        this.biomes = biomes;
        this.sampler = sampler;
    }

    @Override
    public TerraBiome getBiome(double x, double z) {
        return biomes.get(sampler, x, z);
    }
}
