package com.dfsek.terra.addons.biome.pipeline.reimplementation.stage.expander;

import com.dfsek.terra.addons.biome.pipeline.reimplementation.BiomeChunkImpl.ViewPoint;
import com.dfsek.terra.addons.biome.pipeline.reimplementation.api.Expander;
import com.dfsek.terra.addons.biome.pipeline.reimplementation.api.biome.PipelineBiome;


public class DebugExpander implements Expander {
    
    private final PipelineBiome biome;
    
    public DebugExpander(PipelineBiome biome) {
        this.biome = biome;
    }
    
    @Override
    public PipelineBiome fillBiome(ViewPoint viewPoint) {
        return biome;
    }
}
