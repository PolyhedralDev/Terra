package com.dfsek.terra.addons.biome.pipeline.reimplementation.api;

import com.dfsek.terra.addons.biome.pipeline.reimplementation.BiomeChunkImpl.ViewPoint;
import com.dfsek.terra.addons.biome.pipeline.reimplementation.api.biome.PipelineBiome;


public interface Stage {
    PipelineBiome apply(ViewPoint viewPoint);
    
    int maxRelativeReadDistance();
    
    default Iterable<PipelineBiome> getBiomes(Iterable<PipelineBiome> biomes) {
        return biomes;
    }
}
