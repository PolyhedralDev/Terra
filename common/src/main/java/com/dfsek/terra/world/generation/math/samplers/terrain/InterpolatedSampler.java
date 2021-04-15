package com.dfsek.terra.world.generation.math.samplers.terrain;

import com.dfsek.terra.world.generation.math.interpolation.Interpolator3;

public class InterpolatedSampler implements TerrainSampler {
    private final InterpolatorSupplier supplier;

    public InterpolatedSampler(InterpolatorSupplier supplier) {
        this.supplier = supplier;
    }

    @Override
    public Interpolator3 sample(double x, double y, double z) {
        return null;
    }

    @Override
    public int getXResolution() {
        return supplier.getXResolution();
    }

    @Override
    public int getYResolution() {
        return supplier.getYResolution();
    }

    @Override
    public int getZResolution() {
        return supplier.getZResolution();
    }
}
