package com.dfsek.terra.addons.chunkgenerator.palette.slant;

import java.util.List;

import com.dfsek.terra.addons.chunkgenerator.generation.math.SlantCalculationMethod;
import com.dfsek.terra.addons.chunkgenerator.palette.PaletteHolder;


public interface SlantHolder {

    SlantHolder EMPTY = new SlantHolder() {
        @Override
        public boolean isAboveDepth(int depth) {
            return false;
        }

        @Override
        public boolean isInSlantThreshold(double slant) {
            return false;
        }

        @Override
        public PaletteHolder getPalette(double slant) {
            throw new UnsupportedOperationException("Empty holder cannot return a palette");
        }
    };

    static SlantHolder of(List<SlantHolder.Layer> layers, int slantDepth, SlantCalculationMethod calculationMethod) {
        if(layers.isEmpty()) {
            return EMPTY;
        } else if(layers.size() == 1) {
            return new SingleSlantHolder(layers.get(0), slantDepth, calculationMethod);
        }
        return new MultipleSlantHolder(layers, slantDepth, calculationMethod);
    }

    boolean isAboveDepth(int depth);

    boolean isInSlantThreshold(double slant);

    PaletteHolder getPalette(double slant);


    record Layer(PaletteHolder palette, double threshold) {
    }
}
