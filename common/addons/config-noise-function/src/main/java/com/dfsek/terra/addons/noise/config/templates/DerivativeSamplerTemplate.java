package com.dfsek.terra.addons.noise.config.templates;

import com.dfsek.seismic.type.sampler.DerivativeSampler;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.exception.ValidationException;

import com.dfsek.seismic.type.sampler.Sampler;


public class DerivativeSamplerTemplate extends SamplerTemplate<DerivativeSampler> {

    @Value(".")
    private Sampler sampler;

    @Override
    public boolean validate() throws ValidationException {
        if(!DerivativeSampler.isDifferentiable(sampler)) throw new ValidationException(
            "Provided sampler does not support calculating a derivative");
        return super.validate();
    }

    @Override
    public DerivativeSampler get() {
        return (DerivativeSampler) sampler;
    }
}
