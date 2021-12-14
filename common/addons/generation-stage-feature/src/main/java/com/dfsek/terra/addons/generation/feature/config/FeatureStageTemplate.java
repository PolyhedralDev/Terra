package com.dfsek.terra.addons.generation.feature.config;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.generation.feature.FeatureGenerationStage;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.world.chunk.generation.stage.GenerationStage;


public class FeatureStageTemplate implements ObjectTemplate<GenerationStage> {
    private final Platform platform;
    @Value("id")
    private String id;
    
    public FeatureStageTemplate(Platform platform) { this.platform = platform; }
    
    
    @Override
    public FeatureGenerationStage get() {
        return new FeatureGenerationStage(platform, id);
    }
}
