package com.dfsek.terra.addons.chunkgenerator.palette.slant;

import com.dfsek.terra.addons.chunkgenerator.generation.math.samplers.Sampler3D;


public abstract class SlantHolderImpl implements SlantHolder {
    protected final boolean floorToThreshold;
    private final SlantHolder.CalculationMethod calculationMethod;
    private final int slantDepth;
    
    protected SlantHolderImpl(int slantDepth, CalculationMethod calculationMethod) {
        this.floorToThreshold = calculationMethod.floorToThreshold();
        this.calculationMethod = calculationMethod;
        this.slantDepth = slantDepth;
    }
    
    protected abstract double getSlantThreshold();
    
    @Override
    public final double calculateSlant(Sampler3D sampler, double x, double y, double z) {
        return calculationMethod.slant(sampler, x, y, z);
    }
    
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
