package com.dfsek.terra.addons.chunkgenerator.palette.slant;

import com.dfsek.terra.addons.chunkgenerator.generation.math.SlantCalculationMethod;


public abstract class SlantHolderImpl implements SlantHolder {
    protected final boolean floorToThreshold;
    private final int slantDepth;

    protected SlantHolderImpl(int slantDepth, SlantCalculationMethod calculationMethod) {
        this.floorToThreshold = calculationMethod.floorToThreshold();
        this.slantDepth = slantDepth;
    }

    protected abstract double getSlantThreshold();

    @Override
    public final boolean isAboveDepth(int depth) {
        return depth <= slantDepth;
    }

    @Override
    public final boolean isInSlantThreshold(double slant) {
        return (floorToThreshold ?
                slant > getSlantThreshold() :
                slant < getSlantThreshold()
        );
    }
}
