/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.chunkgenerator.palette;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;

import java.lang.reflect.AnnotatedType;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


@SuppressWarnings("unchecked")
public class SlantHolderLoader implements TypeLoader<SlantHolder> {
    @Override
    public SlantHolder load(AnnotatedType type, Object o, ConfigLoader configLoader) throws LoadException {
        List<Map<Object, Object>> layers = (List<Map<Object, Object>>) o;
        TreeMap<Double, PaletteHolder> slantLayers = new TreeMap<>();
        double minThreshold = Double.MAX_VALUE;
        
        for(Map<Object, Object> layer : layers) {
            double threshold = ((Number) layer.get("threshold")).doubleValue();
            if(threshold < minThreshold) minThreshold = threshold;
            slantLayers.put(threshold, configLoader.loadType(PaletteHolder.class, layer.get("palette")));
        }
        
        return new SlantHolder(slantLayers, minThreshold);
    }
}
