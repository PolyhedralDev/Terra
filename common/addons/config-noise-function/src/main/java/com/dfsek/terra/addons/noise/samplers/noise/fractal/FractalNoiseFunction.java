/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.samplers.noise.fractal;

import com.dfsek.terra.addons.noise.samplers.noise.NoiseFunction;
import com.dfsek.terra.api.noise.NoiseSampler;


public abstract class FractalNoiseFunction extends NoiseFunction {
    protected final NoiseSampler input;
    protected double fractalBounding = 1 / 1.75;
    protected int octaves = 3;
    protected double gain = 0.5;
    protected double lacunarity = 2.0d;
    protected double weightedStrength = 0.0d;
    
    public FractalNoiseFunction(NoiseSampler input) {
        this.input = input;
        frequency = 1;
    }
    
    protected void calculateFractalBounding() {
        double gain = fastAbs(this.gain);
        double amp = gain;
        double ampFractal = 1.0;
        for(int i = 1; i < octaves; i++) {
            ampFractal += amp;
            amp *= gain;
        }
        fractalBounding = 1 / ampFractal;
    }
    
    public void setGain(double gain) {
        this.gain = gain;
        calculateFractalBounding();
    }
    
    public void setLacunarity(double lacunarity) {
        this.lacunarity = lacunarity;
    }
    
    public void setOctaves(int octaves) {
        this.octaves = octaves;
        calculateFractalBounding();
    }
    
    public void setWeightedStrength(double weightedStrength) {
        this.weightedStrength = weightedStrength;
    }
}
