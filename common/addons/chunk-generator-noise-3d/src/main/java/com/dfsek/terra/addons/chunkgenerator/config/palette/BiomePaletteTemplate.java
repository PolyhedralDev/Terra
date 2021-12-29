/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.chunkgenerator.config.palette;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Description;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.dfsek.terra.addons.chunkgenerator.palette.PaletteHolder;
import com.dfsek.terra.addons.chunkgenerator.palette.PaletteHolderBuilder;
import com.dfsek.terra.addons.chunkgenerator.palette.SlantHolder;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.world.chunk.generation.util.Palette;


public class BiomePaletteTemplate implements ObjectTemplate<PaletteInfo> {
    @Value("slant")
    @Default
    @Description("The slant palettes to use in this biome.")
    private @Meta List<@Meta SlantLayer> slant = Collections.emptyList();
    
    @Value("slant-depth")
    @Default
    @Description("The maximum depth at which to apply a slant palette.")
    private @Meta int slantDepth = Integer.MAX_VALUE;
    
    @Value("palette")
    @Description("The palettes to use in this biome.")
    private @Meta List<@Meta Map<@Meta Palette, @Meta Integer>> palettes;
    
    @Value("ocean.level")
    @Description("Sea level in this biome.")
    private @Meta int seaLevel;
    
    @Value("ocean.palette")
    @Description("The palette to use for the ocean in this biome.")
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
        
        return new PaletteInfo(builder.build(), new SlantHolder(slantLayers, minThreshold), oceanPalette, seaLevel, slantDepth);
    }
}
