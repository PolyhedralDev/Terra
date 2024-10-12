package com.dfsek.terra.addons.noise.normalizer;

import com.dfsek.terra.api.noise.NoiseSampler;


public class LinearMapNormalizer extends Normalizer {

    private final double aFrom;

    private final double aTo;

    private final double bFrom;

    private final double bTo;

    public LinearMapNormalizer(NoiseSampler sampler, double aFrom, double aTo, double bFrom, double bTo) {
        super(sampler);
        this.aFrom = aFrom;
        this.aTo = aTo;
        this.bFrom = bFrom;
        this.bTo = bTo;
    }

    @Override
    public double normalize(double in) {
        return (in - aFrom) * (aTo - bTo) / (aFrom - bFrom) + aTo;
    }
}
