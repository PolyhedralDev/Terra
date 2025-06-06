package com.dfsek.terra.addons.chunkgenerator.layer.sampler;

import com.dfsek.terra.addons.chunkgenerator.api.LayerSampler;
import com.dfsek.seismic.type.sampler.Sampler;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.info.WorldProperties;


public class SimpleLayerSampler implements LayerSampler {
    
    private Sampler sampler;
    
    public SimpleLayerSampler(Sampler sampler) {
        this.sampler = sampler;
    }
    
    @Override
    public double sample(int x, int y, int z, WorldProperties world, BiomeProvider biomeProvider) {
        return sampler.getSample(world.getSeed(), x, y, z);
    }
}
