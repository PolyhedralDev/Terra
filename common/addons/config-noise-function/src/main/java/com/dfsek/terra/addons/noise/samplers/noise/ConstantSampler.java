/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.samplers.noise;

import java.util.List;


/**
 * Sampler3D implementation that returns a constant.
 */
public class ConstantSampler extends NoiseFunction {
    private final double constant;
    
    public ConstantSampler(double constant) {
        this.constant = constant;
    }
    
    @Override
    public double getNoiseRaw(long seed, double x, double y, List<double[]> context, int contextLayer, int contextRadius) {
        return constant;
    }
    
    @Override
    public double getNoiseRaw(long seed, double x, double y, double z, List<double[]> context, int contextLayer, int contextRadius) {
        return constant;
    }
}
