package com.dfsek.terra.config.builder;

import com.dfsek.terra.api.docs.AutoDocAlias;
import com.dfsek.terra.api.platform.world.Biome;
import com.dfsek.terra.api.util.collections.ProbabilityCollection;
import com.dfsek.terra.api.util.seeded.SeededBuilder;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.config.templates.BiomeTemplate;

@AutoDocAlias("TerraBiome")
public interface BiomeBuilder extends SeededBuilder<TerraBiome> {
    ProbabilityCollection<Biome> getVanillaBiomes();

    BiomeTemplate getTemplate();
}
