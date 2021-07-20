package com.dfsek.terra.addons.noise.config;

import com.dfsek.terra.api.noise.NoiseSampler;

public class DimensionApplicableNoiseSampler {
    private final int dimensions;

    private final NoiseSampler sampler;

    public DimensionApplicableNoiseSampler(int dimensions, NoiseSampler sampler) {
        this.dimensions = dimensions;
        this.sampler = sampler;
    }

    public int getDimensions() {
        return dimensions;
    }

    public NoiseSampler getSampler() {
        return sampler;
    }
}
