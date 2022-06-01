/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.samplers;

import com.dfsek.terra.api.noise.NoiseSampler;


public class KernelSampler implements NoiseSampler {
    private final double[][] kernel;
    private final NoiseSampler in;
    private double frequency = 1;
    
    public KernelSampler(double[][] kernel, NoiseSampler in) {
        this.kernel = kernel;
        this.in = in;
    }
    
    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }
    
    @Override
    public double noise(long seed, double x, double y) {
        x *= frequency;
        y *= frequency;
        double accumulator = 0;
        
        for(int kx = 0; kx < kernel.length; kx++) {
            for(int ky = 0; ky < kernel[kx].length; ky++) {
                double k = kernel[kx][ky];
                if(k != 0) {
                    accumulator += in.noise(seed, x + kx, y + ky) * k;
                }
            }
        }
        
        return accumulator;
    }
    
    @Override
    public double noise(long seed, double x, double y, double z) {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        double accumulator = 0;
        
        for(int kx = 0; kx < kernel.length; kx++) {
            for(int ky = 0; ky < kernel[kx].length; ky++) {
                double k = kernel[kx][ky];
                if(k != 0) {
                    accumulator += in.noise(seed, x + kx, y, z + ky) * k;
                }
            }
        }
        
        return accumulator;
    }
}
