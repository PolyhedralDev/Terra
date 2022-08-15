/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.chunkgenerator.palette;


import java.util.Map.Entry;
import java.util.TreeMap;


public class SlantHolder {
    private final TreeMap<Double, PaletteHolder> layers;
    private final double maxSlant;
    
    private SlantHolder(TreeMap<Double, PaletteHolder> layers, double maxSlant) {
        this.layers = layers;
        this.maxSlant = maxSlant;
    }
    
    public static SlantHolder of(TreeMap<Double, PaletteHolder> layers, double maxSlant) {
        if(layers.size() == 1) {
            Entry<Double, PaletteHolder> firstEntry = layers.firstEntry();
            return new Single(firstEntry.getValue(), maxSlant);
        }
        return new SlantHolder(layers, maxSlant);
    }
    
    public boolean isEmpty() {
        return layers.isEmpty();
    }
    
    public PaletteHolder getPalette(double slant) {
        return layers.ceilingEntry(slant).getValue();
    }
    
    public double getMaxSlant() {
        return maxSlant;
    }
    
    private static final class Single extends SlantHolder {
        
        private final PaletteHolder layers;
        
        public Single(PaletteHolder layers, double maxSlant) {
            super(of(maxSlant, layers), maxSlant);
            this.layers = layers;
        }
        
        private static TreeMap<Double, PaletteHolder> of(double v, PaletteHolder layer) {
            TreeMap<Double, PaletteHolder> map = new TreeMap<>();
            map.put(v, layer);
            return map;
        }
        
        @Override
        public PaletteHolder getPalette(double slant) {
            return layers;
        }
    }
}
