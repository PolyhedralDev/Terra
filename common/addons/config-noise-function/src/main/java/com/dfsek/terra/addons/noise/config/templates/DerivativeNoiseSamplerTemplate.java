package com.dfsek.terra.addons.noise.config.templates;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.exception.ValidationException;

import com.dfsek.terra.api.noise.DerivativeNoiseSampler;
import com.dfsek.terra.api.noise.NoiseSampler;


public class DerivativeNoiseSamplerTemplate extends SamplerTemplate<DerivativeNoiseSampler> {

    @Value(".")
    private NoiseSampler sampler;

    @Override
    public boolean validate() throws ValidationException {
        if (!(sampler instanceof DerivativeNoiseSampler)) throw new ValidationException("Provided sampler does not support calculating a derivative");
        return super.validate();
    }

    @Override
    public DerivativeNoiseSampler get() {
        return (DerivativeNoiseSampler) sampler;
    }
}
