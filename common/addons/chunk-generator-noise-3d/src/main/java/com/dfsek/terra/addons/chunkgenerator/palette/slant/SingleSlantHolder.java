package com.dfsek.terra.addons.chunkgenerator.palette.slant;

import com.dfsek.terra.addons.chunkgenerator.generation.math.SlantCalculationMethod;
import com.dfsek.terra.addons.chunkgenerator.palette.PaletteHolder;


final class SingleSlantHolder extends SlantHolderImpl {

    private final SlantHolder.Layer layer;

    public SingleSlantHolder(SlantHolder.Layer layer, int slantDepth, SlantCalculationMethod calculationMethod) {
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
