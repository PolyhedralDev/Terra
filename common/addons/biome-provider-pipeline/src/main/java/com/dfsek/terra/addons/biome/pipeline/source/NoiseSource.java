package com.dfsek.terra.addons.biome.pipeline.source;

import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.biome.generation.pipeline.BiomeSource;

public class NoiseSource implements BiomeSource {
    private final ProbabilityCollection<TerraBiome> biomes;
    private final NoiseSampler sampler;

    public NoiseSource(ProbabilityCollection<TerraBiome> biomes, NoiseSampler sampler) {
        this.biomes = biomes;
        this.sampler = sampler;
    }

    @Override
    public TerraBiome getBiome(double x, double z, long seed) {
        return biomes.get(sampler, x, z, seed);
    }
}
