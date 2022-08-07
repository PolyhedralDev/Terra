package com.dfsek.terra.addons.biome.pipeline.api;

import com.dfsek.terra.addons.biome.pipeline.pipeline.BiomeChunkImpl.ViewPoint;
import com.dfsek.terra.addons.biome.pipeline.api.biome.PipelineBiome;


public interface Stage {
    PipelineBiome apply(ViewPoint viewPoint);
    
    int maxRelativeReadDistance();
    
    default Iterable<PipelineBiome> getBiomes(Iterable<PipelineBiome> biomes) {
        return biomes;
    }
}
