package com.dfsek.terra.addons.biome.extrusion.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import java.util.List;

import com.dfsek.terra.addons.biome.extrusion.BiomeExtrusionProvider;
import com.dfsek.terra.addons.biome.extrusion.api.Extrusion;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


public class BiomeExtrusionTemplate implements ObjectTemplate<BiomeProvider> {
    @Value("provider")
    private @Meta BiomeProvider provider;
    
    @Value("resolution")
    @Default
    private @Meta int resolution = 4;
    
    @Value("extrusions")
    private @Meta List<@Meta Extrusion> extrusions;
    
    @Override
    public BiomeProvider get() {
        return new BiomeExtrusionProvider(provider, extrusions, resolution);
    }
}
