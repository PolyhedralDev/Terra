package com.dfsek.terra.addons.biome.extrusion.config.extrusions;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.biome.extrusion.api.Extrusion;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.seismic.type.sampler.Sampler;
import com.dfsek.terra.api.util.range.Range;


public abstract class SamplerExtrusionTemplate implements ObjectTemplate<Extrusion> {
    @Value("sampler")
    protected @Meta Sampler sampler;

    @Value("range")
    protected @Meta Range range;
}
