package com.dfsek.terra.addons.chunkgenerator.layer.predicate;

import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.api.world.biome.Biome;


public class RangeLayerPredicate implements LayerPredicate {
    
    private final Range range;
    
    public RangeLayerPredicate(Range range) {
        this.range = range;
    }
    @Override
    public boolean test(long seed, Biome biome, int x, int y, int z) {
        return range.isInRange(y);
    }
}
