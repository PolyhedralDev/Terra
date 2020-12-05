package com.dfsek.terra.generation;

import net.jafama.FastMath;
import org.polydev.gaea.math.ChunkInterpolator;

public class Sampler {
    private final ChunkInterpolator interpolator;
    private final ElevationInterpolator elevationInterpolator;

    public Sampler(ChunkInterpolator interpolator, ElevationInterpolator elevationInterpolator) {
        this.interpolator = interpolator;
        this.elevationInterpolator = elevationInterpolator;
    }

    public double sample(double x, double y, double z) {
        return interpolator.getNoise(x, y, z) + elevationInterpolator.getElevation(FastMath.roundToInt(x), FastMath.roundToInt(z));
    }
}
