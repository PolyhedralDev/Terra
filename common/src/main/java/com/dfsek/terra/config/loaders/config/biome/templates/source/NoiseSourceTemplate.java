package com.dfsek.terra.config.loaders.config.biome.templates.source;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.util.collections.ProbabilityCollection;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.biome.pipeline.source.BiomeSource;
import com.dfsek.terra.api.world.biome.pipeline.source.RandomSource;
import com.dfsek.terra.config.builder.BiomeBuilder;

public class NoiseSourceTemplate extends SourceTemplate {
    @Value("noise")
    private NoiseSeeded noise;

    @Value("biomes")
    private ProbabilityCollection<BiomeBuilder<? extends TerraBiome>> biomes;

    @Override
    public BiomeSource apply(Long seed) {
        return new RandomSource(biomes.map((biome) -> biome.apply(seed), false), noise.apply(seed));
    }
}
