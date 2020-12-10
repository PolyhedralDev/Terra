package com.dfsek.terra.generation;

import com.dfsek.terra.api.gaea.math.ChunkInterpolator;
import net.jafama.FastMath;

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
