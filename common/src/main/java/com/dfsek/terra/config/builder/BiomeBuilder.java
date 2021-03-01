package com.dfsek.terra.config.builder;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.platform.world.Biome;
import com.dfsek.terra.api.util.collections.ProbabilityCollection;
import com.dfsek.terra.api.util.seeded.SeededBuilder;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.world.TerraWorld;

public interface BiomeBuilder<O extends TerraBiome> extends SeededBuilder<O> {
    ProbabilityCollection<Biome> getVanillaBiomes();
}
