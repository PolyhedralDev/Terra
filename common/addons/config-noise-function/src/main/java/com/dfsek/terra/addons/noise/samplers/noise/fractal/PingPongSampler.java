/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.samplers.noise.fractal;

import com.dfsek.terra.api.noise.NoiseSampler;


public class PingPongSampler extends FractalNoiseFunction {
    private final double pingPongStrength;
    
    public PingPongSampler(long salt, NoiseSampler input, int octaves, double gain, double lacunarity, double weightedStrength,
                           double pingPongStrength) {
        super(salt, input, octaves, gain, lacunarity, weightedStrength);
        this.pingPongStrength = pingPongStrength;
    }
    
    
    private static double pingPong(double t) {
        t -= (int) (t * 0.5f) << 1;
        return t < 1 ? t : 2 - t;
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
    
    public static class Builder extends FractalNoiseFunction.Builder<Builder, PingPongSampler> {
        private double pingPongStrength = 2.0;
        
        @Override
        protected Builder self() {
            return this;
        }
        
        public Builder pingPongStrength(double pingPongStrength) {
            this.pingPongStrength = pingPongStrength;
            return self();
        }
    
        @Override
        public PingPongSampler build() {
            return new PingPongSampler(salt, input, octaves, gain, lacunarity, weightedStrength, pingPongStrength);
        }
    }
}
