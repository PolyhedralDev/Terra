package com.dfsek.terra.math;

import com.dfsek.terra.generation.Sampler;

public final class MathUtil {
    private static final double CONST = 0.55;

    public static double derivative(Sampler sampler, double x, double y, double z) {
        double baseSample = sampler.sample(x, y, z);

        double xVal1 = (sampler.sample(x + CONST, y, z) - baseSample) / CONST;
        double xVal2 = (sampler.sample(x - CONST, y, z) - baseSample) / CONST;
        double zVal1 = (sampler.sample(x, y, z + CONST) - baseSample) / CONST;
        double zVal2 = (sampler.sample(x, y, z - CONST) - baseSample) / CONST;
        double yVal1 = (sampler.sample(x, y + CONST, z) - baseSample) / CONST;
        double yVal2 = (sampler.sample(x, y - CONST, z) - baseSample) / CONST;

        return Math.sqrt(((xVal2 - xVal1) * (xVal2 - xVal1)) + ((zVal2 - zVal1) * (zVal2 - zVal1)) + ((yVal2 - yVal1) * (yVal2 - yVal1)));
    }
}
