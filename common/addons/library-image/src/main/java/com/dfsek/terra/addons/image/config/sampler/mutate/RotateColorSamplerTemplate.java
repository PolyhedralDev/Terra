package com.dfsek.terra.addons.image.config.sampler.mutate;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import net.jafama.FastMath;

import com.dfsek.terra.addons.image.sampler.ColorSampler;
import com.dfsek.terra.addons.image.sampler.mutate.RotateColorSampler;


public class RotateColorSamplerTemplate extends MutateColorSamplerTemplate {
    
    @Value("angle")
    private double angle;
    
    @Override
    public ColorSampler get() {
        return new RotateColorSampler(sampler, FastMath.toRadians(angle));
    }
}
