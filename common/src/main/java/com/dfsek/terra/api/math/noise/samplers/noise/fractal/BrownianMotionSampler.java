package com.dfsek.terra.api.math.noise.samplers.noise.fractal;

import com.dfsek.terra.api.math.noise.NoiseSampler;

public class BrownianMotionSampler extends FractalNoiseFunction {
    public BrownianMotionSampler(int seed, NoiseSampler input) {
        super(seed, input);
    }

    @Override
    public double getNoiseSeeded(int seed, double x, double y) {
        double sum = 0;
        double amp = fractalBounding;

        for(int i = 0; i < octaves; i++) {
            double noise = input.getNoiseSeeded(seed++, x, y);
            sum += noise * amp;
            amp *= lerp(1.0, fastMin(noise + 1, 2) * 0.5, weightedStrength);

            x *= lacunarity;
            y *= lacunarity;
            amp *= gain;
        }

        return sum;
    }

    @Override
    public double getNoiseSeeded(int seed, double x, double y, double z) {
        double sum = 0;
        double amp = fractalBounding;

        for(int i = 0; i < octaves; i++) {
            double noise = input.getNoiseSeeded(seed++, x, y, z);
            sum += noise * amp;
            amp *= lerp(1.0, (noise + 1) * 0.5, weightedStrength);

            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            amp *= gain;
        }

        return sum;
    }
}
