package com.dfsek.terra.addons.chunkgenerator.palette.slant;

import com.dfsek.terra.addons.chunkgenerator.palette.PaletteHolder;


final class SingleSlantHolder extends SlantHolderImpl {
    
    private final SlantHolder.Layer layer;
    
    public SingleSlantHolder(SlantHolder.Layer layer, int slantDepth, CalculationMethod calculationMethod) {
        super(slantDepth, calculationMethod);
        this.layer = layer;
    }
    
    @Override
    public PaletteHolder getPalette(double slant) {
        return layer.palette();
    }
    
    @Override
    protected double getSlantThreshold() {
        return layer.threshold();
    }
}
