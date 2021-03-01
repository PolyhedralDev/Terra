package com.dfsek.terra.config.builder;

import com.dfsek.terra.api.platform.world.Biome;
import com.dfsek.terra.api.util.collections.ProbabilityCollection;
import com.dfsek.terra.api.util.seeded.SeededBuilder;
import com.dfsek.terra.api.world.biome.TerraBiome;

public interface BiomeBuilder extends SeededBuilder<TerraBiome> {
    ProbabilityCollection<Biome> getVanillaBiomes();
}
