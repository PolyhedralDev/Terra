/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.samplers.noise.fractal;

import com.dfsek.terra.api.noise.NoiseSampler;


public class PingPongSampler extends FractalNoiseFunction {
    private double pingPongStrength = 2.0;
    
    public PingPongSampler(NoiseSampler input) {
        super(input);
    }
    
    
    private static double pingPong(double t) {
        t -= (int) (t * 0.5f) << 1;
        return t < 1 ? t : 2 - t;
    }
    
    public void setPingPongStrength(double strength) {
        this.pingPongStrength = strength;
    }
    
    @Override
    public double getNoiseRaw(long seed, double x, double y) {
        double sum = 0;
        double amp = fractalBounding;
        
        for(int i = 0; i < octaves; i++) {
            double noise = pingPong((input.noise(seed++, x, y) + 1) * pingPongStrength);
            sum += (noise - 0.5) * 2 * amp;
            amp *= lerp(1.0, noise, weightedStrength);
            
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
            double noise = pingPong((input.noise(seed++, x, y, z) + 1) * pingPongStrength);
            sum += (noise - 0.5) * 2 * amp;
            amp *= lerp(1.0, noise, weightedStrength);
            
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            amp *= gain;
        }
        
        return sum;
    }
}
