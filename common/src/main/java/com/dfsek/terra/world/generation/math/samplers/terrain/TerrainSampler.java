package com.dfsek.terra.world.generation.math.samplers.terrain;

import com.dfsek.terra.world.generation.math.interpolation.Interpolator2;
import com.dfsek.terra.world.generation.math.interpolation.Interpolator3;

public interface TerrainSampler {
    Interpolator3 sample(double x, double y, double z);

    default Interpolator2 sample(double x, double z) {
        return (s, t) -> sample(x, 0, z).interpolate(s, 0, t);
    }

    int getXResolution();

    default int getYResolution() {
        return getXResolution();
    }

    default int getZResolution() {
        return getXResolution();
    }
}
