package com.dfsek.terra.api.util.seeded;

import com.dfsek.terra.api.util.ProbabilityCollection;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.TerraBiome;

public interface BiomeBuilder extends SeededBuilder<TerraBiome> {
    ProbabilityCollection<Biome> getVanillaBiomes();
}
