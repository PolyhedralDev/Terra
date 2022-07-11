package com.dfsek.terra.addons.chunkgenerator.layer.resolve;

import com.dfsek.terra.addons.chunkgenerator.layer.palette.LayerPalette;
import com.dfsek.terra.addons.chunkgenerator.layer.predicate.LayerPredicate;
import com.dfsek.terra.api.world.biome.Biome;


public class PredicateLayerResolver implements LayerResolver {
    
    private final LayerPredicate predicate;
    
    private final LayerResolver trueResolver;
    
    private final LayerResolver falseResolver;
    
    public PredicateLayerResolver(LayerPredicate predicate, LayerResolver trueResolver, LayerResolver falseResolver) {
        this.predicate = predicate;
        this.trueResolver = trueResolver;
        this.falseResolver = falseResolver;
    }
    
    @Override
    public LayerPalette resolve(long seed, Biome biome, int x, int y, int z) {
        return predicate.test(seed, biome, x, y, z) ? trueResolver.resolve(seed, biome, x, y, z) : falseResolver.resolve(seed, biome, x, y, z);
    }
}
