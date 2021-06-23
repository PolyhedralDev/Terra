package com.dfsek.terra.config.loaders.config.biome.templates.source;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.util.ProbabilityCollection;
import com.dfsek.terra.api.util.collections.ProbabilityCollectionImpl;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.api.world.biome.generation.pipeline.BiomeSource;
import com.dfsek.terra.api.world.biome.pipeline.source.RandomSource;
import com.dfsek.terra.config.builder.BiomeBuilder;

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
