package com.dfsek.terra.addons.image.colorsampler.mutate;

import com.dfsek.terra.addons.image.colorsampler.ColorSampler;


public class TranslateColorSampler implements ColorSampler {
    
    private final ColorSampler sampler;
    private final int translateX, translateZ;
    
    public TranslateColorSampler(ColorSampler sampler, int translateX, int translateZ) {
        this.sampler = sampler;
        this.translateX = translateX;
        this.translateZ = translateZ;
    }
    
    @Override
    public Integer apply(int x, int z) {
        return sampler.apply(x - translateX, z - translateZ);
    }
}
