package com.dfsek.terra.addons.biome.image.v2.config.converter.mapping;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import java.util.Map;

import com.dfsek.terra.addons.image.converter.mapping.ColorMapping;
import com.dfsek.terra.addons.image.util.MapUtil;
import com.dfsek.terra.api.world.biome.Biome;


public class DefinedBiomeColorMappingTemplate implements ObjectTemplate<ColorMapping<Biome>> {
    
    @Value("map")
    Map<String, Biome> map;
    
    @Override
    public ColorMapping<Biome> get() {
        var map = MapUtil.mapKeys(this.map, Integer::decode);
        return () -> map;
    }
}
