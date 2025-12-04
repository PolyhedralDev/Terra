package com.dfsek.terra.addons.image.colorsampler.mutate;

import com.dfsek.terra.addons.image.colorsampler.ColorSampler;


public class ScaleColorSampler implements ColorSampler {

    private final ColorSampler sampler;
    private final double scaleX, scaleZ;


    public ScaleColorSampler(ColorSampler sampler, double scaleX, double scaleZ) {
        this.sampler = sampler;
        this.scaleX = scaleX;
        this.scaleZ = scaleZ;
    }

    @Override
    public int apply(int x, int z) {
        int sx = (int) (x / scaleX);
        int sz = (int) (z / scaleZ);
        return sampler.apply(sx, sz);
    }
}
