package com.dfsek.terra.addons.chunkgenerator.layer.sampler.chunk;

import com.dfsek.terra.addons.chunkgenerator.api.LayerSampler;
import com.dfsek.terra.addons.chunkgenerator.api.chunk.ChunkLayerSampler;
import com.dfsek.terra.addons.chunkgenerator.layer.sampler.ElevationLayerSampler;
import com.dfsek.terra.addons.chunkgenerator.layer.sampler.blend.BlendProperties;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.info.WorldProperties;


public class ElevationChunkLayerSampler implements ChunkLayerSampler {
    double[] samples;

    public ElevationChunkLayerSampler(int chunkX, int chunkZ, WorldProperties world, BiomeProvider biomeProvider, ElevationLayerSampler layerSampler,
                                      BlendProperties blendProperties) {
        //I see no reason to implement sparse blending here, elevation is inexpensive. If that changes, it can be easily implemented here.
        samples = new double[16 * 16];

        int xOrigin = chunkX << 4;
        int zOrigin = chunkZ << 4;

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int cx = xOrigin + x;
                int cz = zOrigin + z;
                int index = x * 16 + z;
                samples[index] = layerSampler.sample(cx, 0, cz, world, biomeProvider);
            }
        }
    }

    @Override
    public double sample(int fmX, int y, int fmZ) {
        int index = fmX * 16 + fmZ;
        return samples[index];
    }
}
