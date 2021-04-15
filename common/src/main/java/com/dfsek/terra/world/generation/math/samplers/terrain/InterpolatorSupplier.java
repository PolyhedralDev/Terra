package com.dfsek.terra.world.generation.math.samplers.terrain;

import com.dfsek.terra.world.generation.math.interpolation.Interpolator2;
import com.dfsek.terra.world.generation.math.interpolation.Interpolator3;

import java.util.function.Supplier;

public interface InterpolatorSupplier {
    int getXResolution();
    int getYResolution();
    int getZResolution();

    Interpolator2 get(double t, double v0, double v1);

    Interpolator3 get(double _000, double _100,
                      double _010, double _110,
                      double _001, double _101,
                      double _011, double _111);
}
