package com.dfsek.terra.addons.image.config.sampler;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import net.jafama.FastMath;

import com.dfsek.terra.addons.image.sampler.ColorSampler;
import com.dfsek.terra.addons.image.sampler.RotateColorSampler;


public class RotateColorSamplerTemplate implements ObjectTemplate<ColorSampler> {
    
    @Value("image")
    private ColorSampler sampler;
    
    @Value("angle")
    private double angle;
    
    @Override
    public ColorSampler get() {
        return new RotateColorSampler(sampler, FastMath.toRadians(angle));
    }
}
