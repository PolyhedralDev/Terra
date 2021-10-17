package com.dfsek.terra.addons.chunkgenerator.palette;


import java.util.TreeMap;


public class SlantHolder {
    private final TreeMap<Double, PaletteHolder> layers;
    private final double minSlope;
    
    public SlantHolder(TreeMap<Double, PaletteHolder> layers, double minSlope) {
        this.layers = layers;
        this.minSlope = minSlope;
    }
    
    public PaletteHolder getPalette(double slope) {
        return layers.floorEntry(slope).getValue();
    }
    
    public double getMinSlope() {
        return minSlope;
    }
}
