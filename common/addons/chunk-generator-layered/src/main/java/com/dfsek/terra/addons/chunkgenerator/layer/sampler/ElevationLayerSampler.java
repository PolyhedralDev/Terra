package com.dfsek.terra.addons.chunkgenerator.layer.sampler;

import com.dfsek.seismic.type.sampler.Sampler;

import com.dfsek.terra.addons.chunkgenerator.api.LayerSampler;
import com.dfsek.terra.addons.chunkgenerator.api.chunk.ChunkLayerSampler;
import com.dfsek.terra.addons.chunkgenerator.layer.sampler.blend.BlendProperties;
import com.dfsek.terra.addons.chunkgenerator.layer.sampler.chunk.DensityChunkLayerSampler;
import com.dfsek.terra.addons.chunkgenerator.layer.sampler.chunk.ElevationChunkLayerSampler;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.info.WorldProperties;


public class ElevationLayerSampler implements LayerSampler {

    private final Sampler sampler;
    private final BlendProperties blendProperties;

    public ElevationLayerSampler(Sampler sampler, BlendProperties blendProperties) {
        this.sampler = sampler;
        this.blendProperties = blendProperties;
    }

    @Override
    public double sample(int x, int y, int z, WorldProperties world, BiomeProvider biomeProvider) {
        //TODO if needed make this match chunk impl
        return sampler.getSample(world.getSeed(), x, z);
    }

    @Override
    public ChunkLayerSampler getChunk(int chunkX, int chunkZ, WorldProperties world, BiomeProvider biomeProvider) {
        return new ElevationChunkLayerSampler(chunkX, chunkZ, world, biomeProvider, this, blendProperties);
    }

    @Override
    public double getBlendWeight() {
        return blendProperties.weight();
    }
}
