package com.dfsek.terra.addons.biome.image.v2.config.converter;

import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.image.config.converter.ClosestColorConverterTemplate;
import com.dfsek.terra.addons.image.converter.mapping.ColorMapping;
import com.dfsek.terra.api.world.biome.Biome;


public class ClosestBiomeColorConverterTemplate extends ClosestColorConverterTemplate<Biome> {

    @Value("match")
    private ColorMapping<Biome> match;

    @Override
    protected ColorMapping<Biome> getMapping() {
        return match;
    }
}
