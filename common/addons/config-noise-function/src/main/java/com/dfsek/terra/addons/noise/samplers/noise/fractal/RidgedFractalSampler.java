/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.samplers.noise.fractal;

import com.dfsek.terra.api.noise.NoiseSampler;


public class RidgedFractalSampler extends FractalNoiseFunction {
    
    public RidgedFractalSampler(NoiseSampler input) {
        super(input);
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
}
