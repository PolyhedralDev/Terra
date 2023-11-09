package com.dfsek.terra.addons.biome.pipeline.image.config.converter;

import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.biome.pipeline.v2.api.biome.PipelineBiome;
import com.dfsek.terra.addons.image.config.converter.ClosestColorConverterTemplate;
import com.dfsek.terra.addons.image.converter.mapping.ColorMapping;


public class ClosestPipelineBiomeColorConverterTemplate extends ClosestColorConverterTemplate<PipelineBiome> {

    @Value("match")
    private ColorMapping<PipelineBiome> match;

    @Override
    protected ColorMapping<PipelineBiome> getMapping() {
        return match;
    }
}
