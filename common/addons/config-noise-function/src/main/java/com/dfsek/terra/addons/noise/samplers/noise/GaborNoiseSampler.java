/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.samplers.noise;

import net.jafama.FastMath;

import com.dfsek.terra.addons.noise.samplers.noise.random.WhiteNoiseSampler;


public class GaborNoiseSampler extends NoiseFunction {
    private final WhiteNoiseSampler rand;
    private double k = 1.0;
    private double a = 0.1;
    private double f0 = 0.625;
    private double kernelRadius = (FastMath.sqrt(-FastMath.log(0.05) / Math.PI) / a);
    private double impulsesPerKernel = 64d;
    private double impulseDensity = (impulsesPerKernel / (Math.PI * kernelRadius * kernelRadius));
    private double impulsesPerCell = impulseDensity * kernelRadius * kernelRadius;
    private double g = FastMath.exp(-impulsesPerCell);
    
    private double omega0 = Math.PI * 0.25;
    private boolean isotropic = true;
    
    
    public GaborNoiseSampler() {
        rand = new WhiteNoiseSampler();
    }
    
    private void recalculateRadiusAndDensity() {
        kernelRadius = (FastMath.sqrt(-FastMath.log(0.05) / Math.PI) / this.a);
        impulseDensity = (impulsesPerKernel / (Math.PI * kernelRadius * kernelRadius));
        impulsesPerCell = impulseDensity * kernelRadius * kernelRadius;
        g = FastMath.exp(-impulsesPerCell);
    }
    
    private double gaborNoise(long seed, double x, double y) {
        x /= kernelRadius;
        y /= kernelRadius;
        int xi = fastFloor(x);
        int yi = fastFloor(y);
        double xf = x - xi;
        double yf = y - yi;
        double noise = 0;
        for(int dx = -1; dx <= 1; dx++) {
            for(int dz = -1; dz <= 1; dz++) {
                noise += calculateCell(seed, xi + dx, yi + dz, xf - dx, yf - dz);
            }
        }
        return noise;
    }
    
    private double calculateCell(long seed, int xi, int yi, double x, double y) {
        long mashedSeed = murmur64(31L * xi + yi) + seed;
        
        double gaussianSource = (rand.getNoiseRaw(mashedSeed++) + 1) / 2;
        int impulses = 0;
        while(gaussianSource > g) {
            impulses++;
            gaussianSource *= (rand.getNoiseRaw(mashedSeed++) + 1) / 2;
        }
        
        double noise = 0;
        for(int i = 0; i < impulses; i++) {
            noise += rand.getNoiseRaw(mashedSeed++) * gabor(isotropic ? (rand.getNoiseRaw(mashedSeed++) + 1) * Math.PI : omega0,
                                                            x * kernelRadius, y * kernelRadius);
        }
        return noise;
    }
    
    private double gabor(double omega_0, double x, double y) {
        return k * (FastMath.exp(-Math.PI * (a * a) * (x * x + y * y)) * fastCos(2 * Math.PI * f0 * (x * fastCos(omega_0) + y * fastSin(
                omega_0))));
    }
    
    public void setA(double a) {
        this.a = a;
        recalculateRadiusAndDensity();
    }
    
    public void setDeviation(double k) {
        this.k = k;
    }
    
    public void setFrequency0(double f0) {
        this.f0 = f0;
    }
    
    public void setImpulsesPerKernel(double impulsesPerKernel) {
        this.impulsesPerKernel = impulsesPerKernel;
        recalculateRadiusAndDensity();
    }
    
    public void setIsotropic(boolean isotropic) {
        this.isotropic = isotropic;
    }
    
    public void setRotation(double omega0) {
        this.omega0 = Math.PI * omega0;
    }
    
    @Override
    public double getNoiseRaw(long seed, double x, double z) {
        return gaborNoise(seed, x, z);
    }
    
    @Override
    public double getNoiseRaw(long seed, double x, double y, double z) {
        return gaborNoise(seed, x, z);
    }
}
