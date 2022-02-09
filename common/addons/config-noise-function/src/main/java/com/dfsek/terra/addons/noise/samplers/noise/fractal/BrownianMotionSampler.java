/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.samplers.noise.fractal;

import com.dfsek.terra.api.noise.NoiseSampler;


public class BrownianMotionSampler extends FractalNoiseFunction {
    
    public BrownianMotionSampler(long salt, NoiseSampler input, int octaves, double gain, double lacunarity, double weightedStrength) {
        super(salt, input, octaves, gain, lacunarity, weightedStrength);
    }
    
    @Override
    public double getNoiseRaw(long seed, double x, double y) {
        double sum = 0;
        double amp = fractalBounding;
        
        for(int i = 0; i < octaves; i++) {
            double noise = input.noise(seed++, x, y);
            sum += noise * amp;
            amp *= lerp(1.0, fastMin(noise + 1, 2) * 0.5, weightedStrength);
            
            x *= lacunarity;
            y *= lacunarity;
            amp *= gain;
        }
        
        return sum;
    }
    
    @Override
    public double getNoiseRaw(long seed, double x, double y, double z) {
        double sum = 0;
        double amp = fractalBounding;
        
        for(int i = 0; i < octaves; i++) {
            double noise = input.noise(seed++, x, y, z);
            sum += noise * amp;
            amp *= lerp(1.0, (noise + 1) * 0.5, weightedStrength);
            
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            amp *= gain;
        }
        
        return sum;
    }
    
    public static class Builder extends FractalNoiseFunction.Builder<Builder, BrownianMotionSampler> {
    
        @Override
        protected Builder self() {
            return this;
        }
    
        @Override
        public BrownianMotionSampler build() {
            return new BrownianMotionSampler(salt, input, octaves, gain, lacunarity, weightedStrength);
        }

    }
}
