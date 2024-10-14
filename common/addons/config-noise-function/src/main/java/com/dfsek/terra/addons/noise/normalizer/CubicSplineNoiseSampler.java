package com.dfsek.terra.addons.noise.normalizer;

import com.dfsek.terra.addons.noise.math.CubicSpline;
import com.dfsek.terra.api.noise.NoiseSampler;


public class CubicSplineNoiseSampler extends Normalizer {

    private final CubicSpline spline;

    public CubicSplineNoiseSampler(NoiseSampler sampler, CubicSpline spline) {
        super(sampler);
        this.spline = spline;
    }

    @Override
    public double normalize(double in) {
        return spline.apply(in);
    }
}
