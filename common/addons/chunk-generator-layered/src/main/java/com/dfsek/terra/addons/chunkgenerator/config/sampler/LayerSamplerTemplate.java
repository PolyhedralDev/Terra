package com.dfsek.terra.addons.chunkgenerator.config.sampler;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.chunkgenerator.api.LayerSampler;
import com.dfsek.terra.addons.chunkgenerator.layer.sampler.blend.BlendProperties;
import com.dfsek.terra.api.config.meta.Meta;


public abstract class LayerSamplerTemplate implements ObjectTemplate<LayerSampler> {
    @Value("blend")
    protected @Meta BlendProperties blend;
}
