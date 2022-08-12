package com.dfsek.terra.addons.biome.image.config.converter;

import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.image.config.converter.ExactColorConverterTemplate;
import com.dfsek.terra.addons.image.converter.mapping.ColorMapping;
import com.dfsek.terra.api.world.biome.Biome;


public class ExactBiomeColorConverterTemplate extends ExactColorConverterTemplate<Biome> {
    
    @Value("match")
    private ColorMapping<Biome> match;

    @Value("fallback")
    private Biome fallback;

    @Override
    protected ColorMapping<Biome> getMapping() {
        return match;
    }

    @Override
    protected Biome getFallback() {
        return fallback;
    }
}
