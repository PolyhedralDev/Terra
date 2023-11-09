package com.dfsek.terra.addons.biome.pipeline.image.config.converter.mapping;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import java.util.Map;

import com.dfsek.terra.addons.biome.pipeline.v2.api.biome.PipelineBiome;
import com.dfsek.terra.addons.image.config.ColorLoader.ColorString;
import com.dfsek.terra.addons.image.converter.mapping.ColorMapping;
import com.dfsek.terra.addons.image.util.MapUtil;


public class DefinedPipelineBiomeColorMappingTemplate implements ObjectTemplate<ColorMapping<PipelineBiome>> {

    @Value("map")
    Map<ColorString, PipelineBiome> map;

    @Override
    public ColorMapping<PipelineBiome> get() {
        var map = MapUtil.mapKeys(this.map, ColorString::getColor);
        return () -> map;
    }
}
