package com.dfsek.terra.addons.chunkgenerator.layer.predicate;

import com.dfsek.terra.api.world.biome.Biome;


public interface LayerPredicate {
    boolean test(long seed, Biome biome, int x, int y, int z);
}
