package com.dfsek.terra.api.util.seeded;

import com.dfsek.terra.api.properties.PropertyHolder;
import com.dfsek.terra.api.properties.annotations.Linked;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.TerraBiome;

@Linked(TerraBiome.class)
public interface SeededTerraBiome extends SeededBuilder<TerraBiome>, PropertyHolder {
    ProbabilityCollection<Biome> getVanillaBiomes();
}
