package com.dfsek.terra.addons.chunkgenerator.layer.predicate;

import com.dfsek.terra.api.world.biome.Biome;


public class BelowLayerPredicate implements LayerPredicate {
    
    private final int y;
    
    public BelowLayerPredicate(int y) {
        this.y = y;
    }
    
    @Override
    public boolean test(long seed, Biome biome, int x, int y, int z) {
        return y < this.y;
    }
}
