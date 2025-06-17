package com.dfsek.terra.addons.chunkgenerator.config.sampler.blend;

import com.dfsek.seismic.type.sampler.DerivativeSampler;
import com.dfsek.tectonic.api.config.template.ValidatedConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.tectonic.api.exception.ValidationException;

import com.dfsek.terra.addons.chunkgenerator.layer.sampler.blend.BlendProperties;
import com.dfsek.terra.api.config.meta.Meta;


public class BlendPropertiesConfig implements ValidatedConfigTemplate, ObjectTemplate<BlendProperties> {
    @Value("density")
    @Default
    private @Meta int density = 3;

    @Value("weight")
    @Default
    private @Meta double weight = 1;

    @Override
    public boolean validate() throws ValidationException {
        return density > 1 && weight > 1 && density % 18 == 0;
    }

    @Override
    public BlendProperties get() {
        return BlendProperties.of(density, weight);
    }
}
