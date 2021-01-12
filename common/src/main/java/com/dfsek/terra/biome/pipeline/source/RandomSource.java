package com.dfsek.terra.biome.pipeline.source;

import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.math.noise.samplers.NoiseSampler;
import com.dfsek.terra.api.world.biome.Biome;

public class RandomSource implements BiomeSource {
    private final ProbabilityCollection<Biome> biomes;
    private final NoiseSampler sampler;

    public RandomSource(ProbabilityCollection<Biome> biomes, NoiseSampler sampler) {
        this.biomes = biomes;
        this.sampler = sampler;
    }

    @Override
    public Biome getBiome(int x, int z) {
        return biomes.get(sampler, x, z);
    }
}
