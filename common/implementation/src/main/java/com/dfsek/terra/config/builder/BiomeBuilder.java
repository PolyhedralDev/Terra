package com.dfsek.terra.config.builder;

import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.util.collections.ProbabilityCollectionImpl;
import com.dfsek.terra.api.util.seeded.SeededBuilder;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.config.templates.BiomeTemplate;

public interface BiomeBuilder extends SeededBuilder<TerraBiome> {
    ProbabilityCollectionImpl<Biome> getVanillaBiomes();

    BiomeTemplate getTemplate();
}
