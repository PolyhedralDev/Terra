package com.dfsek.terra.addons.biome.pipeline.v2.api;

import com.dfsek.terra.addons.biome.pipeline.v2.api.biome.PipelineBiome;
import com.dfsek.terra.addons.biome.pipeline.v2.pipeline.BiomeChunkImpl.ViewPoint;


public interface Stage {
    PipelineBiome apply(ViewPoint viewPoint);
    
    int maxRelativeReadDistance();
    
    default Iterable<PipelineBiome> getBiomes(Iterable<PipelineBiome> biomes) {
        return biomes;
    }
}
