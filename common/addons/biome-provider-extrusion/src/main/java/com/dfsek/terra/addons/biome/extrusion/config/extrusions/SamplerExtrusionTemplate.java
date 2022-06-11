package com.dfsek.terra.addons.biome.extrusion.config.extrusions;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.biome.extrusion.api.Extrusion;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.Range;


public abstract class SamplerExtrusionTemplate implements ObjectTemplate<Extrusion> {
    @Value("sampler")
    protected @Meta NoiseSampler sampler;
    
    @Value("range")
    protected @Meta Range range;
}
