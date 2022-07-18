package com.dfsek.terra.addons.chunkgenerator.layer.predicate;

import com.dfsek.terra.addons.chunkgenerator.api.LayerPredicate;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.info.WorldProperties;


public class BelowLayerPredicate implements LayerPredicate {
    
    private final int y;
    
    public BelowLayerPredicate(int y) {
        this.y = y;
    }
    
    @Override
    public boolean test(int x, int y, int z, WorldProperties properties, BiomeProvider biomeProvider) {
        return y < this.y;
    }
}
