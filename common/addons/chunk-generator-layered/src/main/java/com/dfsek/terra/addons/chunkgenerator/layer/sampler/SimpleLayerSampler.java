package com.dfsek.terra.addons.chunkgenerator.layer.sampler;

import com.dfsek.terra.addons.chunkgenerator.api.LayerSampler;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.info.WorldProperties;


public class SimpleLayerSampler implements LayerSampler {
    
    private NoiseSampler sampler;
    
    public SimpleLayerSampler(NoiseSampler sampler) {
        this.sampler = sampler;
    }
    
    @Override
    public double sample(int x, int y, int z, WorldProperties world, BiomeProvider biomeProvider) {
        return sampler.noise(world.getSeed(), x, y, z);
    }
}
