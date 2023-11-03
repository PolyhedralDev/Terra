package com.dfsek.terra.addons.biome.pipeline.image.config.converter;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.biome.pipeline.v2.api.biome.PipelineBiome;
import com.dfsek.terra.addons.image.config.converter.ExactColorConverterTemplate;
import com.dfsek.terra.addons.image.converter.mapping.ColorMapping;


public class ExactPipelineBiomeColorConverterTemplate extends ExactColorConverterTemplate<PipelineBiome> {
    
    @Value("match")
    private ColorMapping<PipelineBiome> match;
    
    @Value("else")
    private PipelineBiome fallback;
    
    @Value("ignore-alpha")
    @Default
    private boolean ignoreAlpha = true;
    
    @Override
    protected ColorMapping<PipelineBiome> getMapping() {
        return match;
    }
    
    @Override
    protected PipelineBiome getFallback() {
        return fallback;
    }
    
    @Override
    protected boolean ignoreAlpha() {
        return ignoreAlpha;
    }
}
