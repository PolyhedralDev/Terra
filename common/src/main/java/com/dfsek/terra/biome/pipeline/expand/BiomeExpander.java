package com.dfsek.terra.biome.pipeline.expand;

import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.biome.pipeline.Position;

public interface BiomeExpander {
    TerraBiome getBetween(Position center, TerraBiome... others);
}
