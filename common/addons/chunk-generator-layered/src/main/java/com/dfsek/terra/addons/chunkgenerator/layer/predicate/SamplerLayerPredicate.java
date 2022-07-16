package com.dfsek.terra.addons.chunkgenerator.layer.predicate;

import com.dfsek.terra.addons.chunkgenerator.api.LayerPredicate;
import com.dfsek.terra.addons.chunkgenerator.api.LayerSampler;
import com.dfsek.terra.api.world.biome.Biome;


public class SamplerLayerPredicate implements LayerPredicate {
    
    private final LayerSampler sampler;
    
    private final double threshold;
    
    public SamplerLayerPredicate(LayerSampler sampler, double threshold) {
        this.sampler = sampler;
        this.threshold = threshold;
    }
    
    @Override
    public boolean test(long seed, Biome biome, int x, int y, int z) {
        return sampler.sample(seed, biome, x, y, z) > threshold;
    }
}
