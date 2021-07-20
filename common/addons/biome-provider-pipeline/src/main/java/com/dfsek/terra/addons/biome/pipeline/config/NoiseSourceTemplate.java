package com.dfsek.terra.addons.biome.pipeline.config;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.addons.biome.pipeline.source.RandomSource;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.util.seeded.SeededTerraBiome;
import com.dfsek.terra.api.util.seeded.SeededNoiseSampler;
import com.dfsek.terra.api.world.biome.generation.pipeline.BiomeSource;

public class NoiseSourceTemplate extends SourceTemplate {
    @Value("noise")
    private NoiseSampler noise;

    @Value("biomes")
    private ProbabilityCollection<SeededTerraBiome> biomes;

    @Override
    public BiomeSource build(long seed) {
        return new RandomSource(biomes.map((biome) -> biome.build(seed), false), noise);
    }
}
