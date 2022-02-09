/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.samplers.noise.fractal;

import com.dfsek.terra.addons.noise.samplers.noise.NoiseFunction;
import com.dfsek.terra.api.noise.NoiseSampler;

import java.util.Objects;


public abstract class FractalNoiseFunction extends NoiseFunction {
    protected final NoiseSampler input;
    protected final double fractalBounding;
    protected final int octaves;
    protected final double gain;
    protected final double lacunarity;
    protected final double weightedStrength;
    
    public FractalNoiseFunction(long salt, NoiseSampler input, int octaves, double gain, double lacunarity,
                                double weightedStrength) {
        super(1, salt);
    
        this.input = Objects.requireNonNull(input);
        this.octaves = octaves;
        this.gain = gain;
        this.lacunarity = lacunarity;
        this.weightedStrength = weightedStrength;
    
        double gain0 = fastAbs(this.gain);
        double amp = gain0;
        double ampFractal = 1.0;
        for(int i = 1; i < octaves; i++) {
            ampFractal += amp;
            amp *= gain0;
        }
        fractalBounding = 1 / ampFractal;
    }
    
    public static abstract class Builder<BUILDER_TYPE, TYPE> extends NoiseFunction.Builder<BUILDER_TYPE, TYPE> {
        protected NoiseSampler input = null;
        protected int octaves = 3;
        protected double gain = 0.5;
        protected double lacunarity = 2.0d;
        protected double weightedStrength = 0.0d;
        
        public Builder() {
        }
        
        public BUILDER_TYPE input(NoiseSampler input) {
            this.input = input;
            return self();
        }
        
        public BUILDER_TYPE octaves(int octaves) {
            this.octaves = octaves;
            return self();
        }
        
        public BUILDER_TYPE gain(double gain) {
            this.gain = gain;
            return self();
        }
        
        public BUILDER_TYPE lacunarity(double lacunarity) {
            this.lacunarity = lacunarity;
            return self();
        }
        
        public BUILDER_TYPE weightedStrength(double weightedStrength) {
            this.weightedStrength = weightedStrength;
            return self();
        }
        
    }
    
}
