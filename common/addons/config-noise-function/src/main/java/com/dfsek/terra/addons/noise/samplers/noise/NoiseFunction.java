/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.samplers.noise;

import net.jafama.FastMath;

import com.dfsek.terra.api.noise.NoiseSampler;


@SuppressWarnings("ManualMinMaxCalculation")
public abstract class NoiseFunction implements NoiseSampler {
    // Hashing
    protected static final int PRIME_X = 501125321;
    protected static final int PRIME_Y = 1136930381;
    protected static final int PRIME_Z = 1720413743;
    static final int precision = 100;
    static final int modulus = 360 * precision;
    static final double[] sin = new double[360 * 100]; // lookup table
    static {
        for(int i = 0; i < sin.length; i++) {
            sin[i] = (float) Math.sin((double) (i) / (precision));
        }
    }
    protected double frequency = 0.02d;
    protected long salt;
    
    public NoiseFunction() {
        this.salt = 0;
    }
    
    protected static int fastFloor(double f) {
        return f >= 0 ? (int) f : (int) f - 1;
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
    
    protected static int fastRound(double f) {
        return f >= 0 ? (int) (f + 0.5f) : (int) (f - 0.5);
    }
    
    protected static double lerp(double a, double b, double t) {
        return a + t * (b - a);
    }
    
    protected static double interpHermite(double t) {
        return t * t * (3 - 2 * t);
    }
    
    protected static double interpQuintic(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }
    
    protected static double cubicLerp(double a, double b, double c, double d, double t) {
        double p = (d - c) - (a - b);
        return t * t * t * p + t * t * ((a - b) - p) + t * (c - a) + b;
    }
    
    protected static double fastMin(double a, double b) {
        return a < b ? a : b;
    }
    
    protected static double fastMax(double a, double b) {
        return a > b ? a : b;
    }
    
    protected static double fastAbs(double f) {
        return f < 0 ? -f : f;
    }
    
    protected static double fastSqrt(double f) {
        return FastMath.sqrt(f);
    }
    
    protected static int fastCeil(double f) {
        int i = (int) f;
        if(i < f) i++;
        return i;
    }
    
    /**
     * Murmur64 hashing function
     *
     * @param h Input value
     *
     * @return Hashed value
     */
    protected static long murmur64(long h) {
        h ^= h >>> 33;
        h *= 0xff51afd7ed558ccdL;
        h ^= h >>> 33;
        h *= 0xc4ceb9fe1a85ec53L;
        h ^= h >>> 33;
        return h;
    }
    
    protected static double fastSin(double a) {
        return sinLookup((int) (a * precision + 0.5f));
    }
    
    protected static double fastCos(double a) {
        return sinLookup((int) ((a + Math.PI / 2) * precision + 0.5f));
    }
    
    private static double sinLookup(int a) {
        return a >= 0 ? sin[a % (modulus)] : -sin[-a % (modulus)];
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
