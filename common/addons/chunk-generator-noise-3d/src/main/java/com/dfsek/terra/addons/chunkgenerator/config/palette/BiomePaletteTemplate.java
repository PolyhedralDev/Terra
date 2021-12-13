/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.chunkgenerator.config.palette;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.chunkgenerator.palette.PaletteHolder;
import com.dfsek.terra.addons.chunkgenerator.palette.PaletteHolderBuilder;
import com.dfsek.terra.addons.chunkgenerator.palette.SlantHolder;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.world.chunk.generation.util.Palette;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;


public class BiomePaletteTemplate implements ObjectTemplate<PaletteInfo> {
    @Value("slant")
    @Default
    private @Meta List<@Meta SlantLayer> slant = Collections.emptyList();
    @Value("palette")
    private @Meta List<@Meta Map<@Meta Palette, @Meta Integer>> palettes;
    @Value("ocean.level")
    private @Meta int seaLevel;
    
    @Value("ocean.palette")
    private @Meta Palette oceanPalette;
    
    @Override
    public PaletteInfo get() {
        PaletteHolderBuilder builder = new PaletteHolderBuilder();
        for(Map<Palette, Integer> layer : palettes) {
            for(Entry<Palette, Integer> entry : layer.entrySet()) {
                builder.add(entry.getValue(), entry.getKey());
            }
        }
    
        TreeMap<Double, PaletteHolder> slantLayers = new TreeMap<>();
        double minThreshold = Double.MAX_VALUE;
    
        for(SlantLayer layer : slant) {
            double threshold = layer.getThreshold();
            if(threshold < minThreshold) minThreshold = threshold;
            slantLayers.put(threshold, layer.getPalette());
        }
        
        return new PaletteInfo(builder.build(), new SlantHolder(slantLayers, minThreshold), oceanPalette, seaLevel);
    }
}
