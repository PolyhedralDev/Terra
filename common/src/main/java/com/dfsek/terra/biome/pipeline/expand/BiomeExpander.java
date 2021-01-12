package com.dfsek.terra.biome.pipeline.expand;

import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.biome.pipeline.Position;

public interface BiomeExpander {
    Biome getBetween(Position center, Biome... others);
}
