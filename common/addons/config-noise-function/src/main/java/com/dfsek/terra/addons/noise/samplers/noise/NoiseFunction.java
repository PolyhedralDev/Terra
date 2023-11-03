/*
 * Copyright (c) 2020-2023 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.samplers.noise;

import com.dfsek.terra.api.noise.NoiseSampler;


public abstract class NoiseFunction implements NoiseSampler {
    // Hashing
    protected static final int PRIME_X = 501125321;
    protected static final int PRIME_Y = 1136930381;
    protected static final int PRIME_Z = 1720413743;

    protected double frequency = 0.02d;
    protected long salt;
    
    public NoiseFunction() {
        this.salt = 0;
    }
    
    protected static int hash(int seed, int xPrimed, int yPrimed, int zPrimed) {
        int hash = seed ^ xPrimed ^ yPrimed ^ zPrimed;
        
        hash *= 0x27d4eb2d;
        return hash;
    }
    
    protected static int hash(int seed, int xPrimed, int yPrimed) {
        int hash = seed ^ xPrimed ^ yPrimed;
        
        hash *= 0x27d4eb2d;
        return hash;
    }
    
    public void setSalt(long salt) {
        this.salt = salt;
    }
    
    public double getFrequency() {
        return frequency;
    }
    
    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }
    
    @Override
    public double noise(long seed, double x, double y) {
        return getNoiseRaw(seed + salt, x * frequency, y * frequency);
    }
    
    @Override
    public double noise(long seed, double x, double y, double z) {
        return getNoiseRaw(seed + salt, x * frequency, y * frequency, z * frequency);
    }
    
    public abstract double getNoiseRaw(long seed, double x, double y);
    
    public abstract double getNoiseRaw(long seed, double x, double y, double z);
}
