package com.dfsek.terra.addons.chunkgenerator.layer.predicate;

import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.world.biome.Biome;


public class SamplerLayerPredicate implements LayerPredicate {
    
    private final NoiseSampler sampler;
    
    public SamplerLayerPredicate(NoiseSampler sampler) {
        this.sampler = sampler;
    }
    
    
    @Override
    public boolean test(long seed, Biome biome, int x, int y, int z) {
        return sampler.noise(seed, x, y, z) > 0;
    }
}
