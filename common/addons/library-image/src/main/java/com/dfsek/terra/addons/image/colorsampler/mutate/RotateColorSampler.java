package com.dfsek.terra.addons.image.colorsampler.mutate;

import net.jafama.FastMath;

import com.dfsek.terra.addons.image.colorsampler.ColorSampler;


public class RotateColorSampler implements ColorSampler {
    
    private final ColorSampler sampler;
    
    private final double radians;
    
    public RotateColorSampler(ColorSampler sampler, double radians) {
        this.sampler = sampler;
        this.radians = radians;
    }
    
    @Override
    public int apply(int x, int z) {
        return sampler.apply(
            (int) (x * FastMath.cos(radians) - z * FastMath.sin(radians)),
            (int) (z * FastMath.cos(radians) + x * FastMath.sin(radians))
        );
    }
}
