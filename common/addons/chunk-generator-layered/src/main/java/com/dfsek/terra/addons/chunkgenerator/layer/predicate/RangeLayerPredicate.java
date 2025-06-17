package com.dfsek.terra.addons.chunkgenerator.layer.predicate;

import com.dfsek.terra.addons.chunkgenerator.api.LayerPredicate;
import com.dfsek.terra.api.util.range.Range;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.info.WorldProperties;


public class RangeLayerPredicate implements LayerPredicate {
    
    private final Range range;
    
    public RangeLayerPredicate(Range range) {
        this.range = range;
    }
    @Override
    public boolean test(int x, int y, int z, WorldProperties worldProperties, BiomeProvider biomeProvider) {
        return range.isInRange(y);
    }
}
