/*
 * Copyright (c) 2020-2023 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.chunkgenerator.palette.slant;


import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.dfsek.terra.addons.chunkgenerator.palette.PaletteHolder;


public class MultipleSlantHolder extends SlantHolderImpl {
    private final NavigableMap<Double, PaletteHolder> layers;
    private final double slantThreshold;

    MultipleSlantHolder(List<SlantHolder.Layer> slant, int slantDepth, CalculationMethod calculationMethod) {
        super(slantDepth, calculationMethod);
        NavigableMap<Double, PaletteHolder> layers = new TreeMap<>(
            slant.stream().collect(Collectors.toMap(SlantHolder.Layer::threshold, SlantHolder.Layer::palette)));
        Stream<Double> thresholds = layers.keySet().stream();
        double slantThreshold = floorToThreshold ?
                                thresholds.min(Double::compare).orElseThrow() :
                                thresholds.max(Double::compare).orElseThrow();
        this.layers = layers;
        this.slantThreshold = slantThreshold;
    }

    @Override
    protected double getSlantThreshold() {
        return slantThreshold;
    }

    @Override
    public PaletteHolder getPalette(double slant) {
        return (floorToThreshold ?
                layers.floorEntry(slant) :
                layers.ceilingEntry(slant)
        ).getValue();
    }
}
