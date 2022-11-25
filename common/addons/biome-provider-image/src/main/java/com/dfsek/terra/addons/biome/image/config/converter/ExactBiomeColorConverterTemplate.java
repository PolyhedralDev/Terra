package com.dfsek.terra.addons.biome.image.config.converter;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.image.config.converter.ExactColorConverterTemplate;
import com.dfsek.terra.addons.image.converter.mapping.ColorMapping;
import com.dfsek.terra.api.world.biome.Biome;


public class ExactBiomeColorConverterTemplate extends ExactColorConverterTemplate<Biome> {
    
    @Value("match")
    private ColorMapping<Biome> match;

    @Value("else")
    private Biome fallback;
    
    @Value("ignore-alpha")
    @Default
    private boolean ignoreAlpha = true;

    @Override
    protected ColorMapping<Biome> getMapping() {
        return match;
    }

    @Override
    protected Biome getFallback() {
        return fallback;
    }
    
    @Override
    protected boolean ignoreAlpha() {
        return ignoreAlpha;
    }
}
