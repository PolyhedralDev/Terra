package com.dfsek.terra.addons.chunkgenerator.layer.predicate;

import com.dfsek.terra.addons.chunkgenerator.api.LayerPredicate;
import com.dfsek.terra.addons.chunkgenerator.api.LayerSampler;
import com.dfsek.terra.addons.chunkgenerator.math.BooleanOperator;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.info.WorldProperties;


public class SamplerLayerPredicate implements LayerPredicate {
    
    private final LayerSampler sampler;
    
    private final double threshold;
    
    private final BooleanOperator operator;
    
    public SamplerLayerPredicate(LayerSampler sampler, BooleanOperator operator, double threshold) {
        this.sampler = sampler;
        this.operator = operator;
        this.threshold = threshold;
    }
    
    @Override
    public boolean test(int x, int y, int z, WorldProperties worldProperties, BiomeProvider biomeProvider) {
        return operator.evaluate(sampler.sample(x, y, z, worldProperties, biomeProvider), threshold);
    }
    
}
