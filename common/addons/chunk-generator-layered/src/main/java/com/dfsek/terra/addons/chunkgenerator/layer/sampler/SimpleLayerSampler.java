package com.dfsek.terra.addons.chunkgenerator.layer.sampler;

import com.dfsek.terra.addons.chunkgenerator.api.LayerSampler;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.world.biome.Biome;


public class SimpleLayerSampler implements LayerSampler {
    
    private NoiseSampler sampler;
    
    public SimpleLayerSampler(NoiseSampler sampler) {
        this.sampler = sampler;
    }
    
    @Override
    public double sample(long seed, Biome biome, int x, int y, int z) {
        return sampler.noise(seed, x, y, z);
    }
    
    @Override
    public NoiseSampler getSampler() {
        return sampler;
    }
}
