package com.dfsek.terra.addons.chunkgenerator.layer.resolve;

import com.dfsek.terra.addons.chunkgenerator.api.LayerPalette;
import com.dfsek.terra.addons.chunkgenerator.api.LayerPredicate;
import com.dfsek.terra.addons.chunkgenerator.api.LayerResolver;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.info.WorldProperties;


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
    public LayerPalette resolve(int x, int y, int z, WorldProperties world, BiomeProvider biomeProvider) {
        return predicate.test(x, y, z, world, biomeProvider) ? trueResolver.resolve(x, y, z, world, biomeProvider) : falseResolver.resolve(x, y, z, world, biomeProvider);
    }
}
