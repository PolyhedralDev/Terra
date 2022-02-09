/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.samplers.noise.fractal;

import com.dfsek.terra.api.noise.NoiseSampler;


public class RidgedFractalSampler extends FractalNoiseFunction {
    
    public RidgedFractalSampler(long salt, NoiseSampler input, int octaves, double gain, double lacunarity, double weightedStrength) {
        super(salt, input, octaves, gain, lacunarity, weightedStrength);
    }
    
    @Override
    public double getNoiseRaw(long seed, double x, double y) {
        double sum = 0;
        double amp = fractalBounding;
        
        for(int i = 0; i < octaves; i++) {
            double noise = fastAbs(input.noise(seed++, x, y));
            sum += (noise * -2 + 1) * amp;
            amp *= lerp(1.0, 1 - noise, weightedStrength);
            
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
            double noise = fastAbs(input.noise(seed++, x, y, z));
            sum += (noise * -2 + 1) * amp;
            amp *= lerp(1.0, 1 - noise, weightedStrength);
            
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            amp *= gain;
        }
        
        return sum;
    }
    
    public static class Builder extends FractalNoiseFunction.Builder<Builder, RidgedFractalSampler> {
        
        @Override
        protected Builder self() {
            return this;
        }
        
        @Override
        public RidgedFractalSampler build() {
            return new RidgedFractalSampler(salt, input, octaves, gain, lacunarity, weightedStrength);
        }
        
    }
}
