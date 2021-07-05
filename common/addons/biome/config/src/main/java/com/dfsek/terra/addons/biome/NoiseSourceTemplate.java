package com.dfsek.terra.addons.biome;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.util.ProbabilityCollection;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.api.world.biome.generation.pipeline.BiomeSource;
import com.dfsek.terra.addons.biome.pipeline.source.RandomSource;
import com.dfsek.terra.api.util.seeded.BiomeBuilder;

public class NoiseSourceTemplate extends SourceTemplate {
    @Value("noise")
    private NoiseSeeded noise;

    @Value("biomes")
    private ProbabilityCollection<BiomeBuilder> biomes;

    @Override
    public BiomeSource apply(Long seed) {
        return new RandomSource(biomes.map((biome) -> biome.apply(seed), false), noise.apply(seed));
    }
}
