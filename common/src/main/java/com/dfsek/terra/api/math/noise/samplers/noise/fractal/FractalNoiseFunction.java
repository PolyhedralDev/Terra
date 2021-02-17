package com.dfsek.terra.api.math.noise.samplers.noise.fractal;

import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.math.noise.samplers.noise.NoiseFunction;

public abstract class FractalNoiseFunction extends NoiseFunction {
    protected final NoiseSampler input;
    protected double fractalBounding = 1 / 1.75;
    protected int octaves = 3;
    protected double gain = 0.5;
    protected double lacunarity = 2.0d;
    protected double weightedStrength = 0.0d;

    public FractalNoiseFunction(int seed, NoiseSampler input) {
        super(seed);
        this.input = input;
    }

    public void setWeightedStrength(double weightedStrength) {
        this.weightedStrength = weightedStrength;
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

    public void setOctaves(int octaves) {
        this.octaves = octaves;
        calculateFractalBounding();
    }

    public void setGain(double gain) {
        this.gain = gain;
        calculateFractalBounding();
    }

    public void setLacunarity(double lacunarity) {
        this.lacunarity = lacunarity;
    }
}
