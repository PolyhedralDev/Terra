package com.dfsek.terra.addons.generation.feature.config;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;

import com.dfsek.terra.addons.generation.feature.FeatureGenerationStage;
import com.dfsek.terra.api.Platform;


public class FeatureStageTemplate implements ObjectTemplate<FeatureGenerationStage> {
    @Value("id")
    private String id;
    
    private final Platform platform;
    
    public FeatureStageTemplate(Platform platform) { this.platform = platform; }
    
    
    @Override
    public FeatureGenerationStage get() {
        return new FeatureGenerationStage(platform, id);
    }
}
