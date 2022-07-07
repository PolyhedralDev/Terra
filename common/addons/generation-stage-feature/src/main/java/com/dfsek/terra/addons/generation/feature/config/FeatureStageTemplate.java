package com.dfsek.terra.addons.generation.feature.config;

import com.dfsek.tectonic.api.config.template.ValidatedConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import com.dfsek.tectonic.api.exception.ValidationException;

import com.dfsek.terra.addons.generation.feature.FeatureGenerationStage;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.properties.PropertyKey;
import com.dfsek.terra.api.world.chunk.generation.stage.GenerationStage;


public class FeatureStageTemplate implements ObjectTemplate<GenerationStage>, ValidatedConfigTemplate {
    private final Platform platform;
    private final PropertyKey<BiomeFeatures> biomeFeaturesKey;
    @Value("id")
    private String id;
    
    @Value("resolution")
    @Default
    private int resolution = 4;
    
    public FeatureStageTemplate(Platform platform, PropertyKey<BiomeFeatures> biomeFeaturesKey) {
        this.platform = platform;
        this.biomeFeaturesKey = biomeFeaturesKey;
    }
    
    
    @Override
    public FeatureGenerationStage get() {
        return new FeatureGenerationStage(platform, id, resolution, biomeFeaturesKey);
    }
    
    @Override
    public boolean validate() throws ValidationException {
        if(!(resolution == 1
             || resolution == 2
             || resolution == 4
             || resolution == 8
             || resolution == 16)) throw new ValidationException(
                "Resolution must be power of 2 less than or equal to 16 (1, 2, 4, 8, 16), got: " + resolution);
        return true;
    }
}
