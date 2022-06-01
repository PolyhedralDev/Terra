package com.dfsek.terra.addons.generation.structure;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.world.chunk.generation.ProtoWorld;
import com.dfsek.terra.api.world.chunk.generation.stage.GenerationStage;


public class StructureGenerationStage implements GenerationStage {
    private final Platform platform;
    
    public StructureGenerationStage(Platform platform) { this.platform = platform; }
    
    @Override
    public void populate(ProtoWorld world) {
    
    }
}
