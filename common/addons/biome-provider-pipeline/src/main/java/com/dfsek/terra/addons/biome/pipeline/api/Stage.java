package com.dfsek.terra.addons.biome.pipeline.api;

import com.dfsek.terra.addons.biome.pipeline.api.biome.PipelineBiome;
import com.dfsek.terra.addons.biome.pipeline.pipeline.BiomeChunkImpl.ViewPoint;


public interface Stage {
    PipelineBiome apply(ViewPoint viewPoint);
    
    int maxRelativeReadDistance();
    
    default Iterable<PipelineBiome> getBiomes(Iterable<PipelineBiome> biomes) {
        return biomes;
    }
}
