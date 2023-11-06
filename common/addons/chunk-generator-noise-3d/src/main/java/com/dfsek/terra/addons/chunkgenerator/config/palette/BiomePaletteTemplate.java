/*
 * Copyright (c) 2020-2023 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.chunkgenerator.config.palette;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Description;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.chunkgenerator.palette.BiomePaletteInfo;
import com.dfsek.terra.addons.chunkgenerator.palette.PaletteHolder;
import com.dfsek.terra.addons.chunkgenerator.palette.slant.SlantHolder;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.world.chunk.generation.util.Palette;

import java.util.Collections;
import java.util.List;
import java.util.Map;


public class BiomePaletteTemplate implements ObjectTemplate<BiomePaletteInfo> {
    private final Platform platform;
    private final SlantHolder.CalculationMethod slantCalculationMethod;
    @Value("slant")
    @Default
    @Description("The slant palettes to use in this biome.")
    private @Meta List<SlantHolder.@Meta Layer> slantLayers = Collections.emptyList();
    @Value("slant-depth")
    @Default
    @Description("The maximum depth at which to apply a slant palette.")
    private @Meta int slantDepth = Integer.MAX_VALUE;
    @Value("palette")
    @Description("The palettes to use in this biome.")
    private @Meta List<@Meta Map<@Meta Palette, @Meta Integer>> palettes;
    @Value("ocean.level")
    @Description("Sea level in this biome. Defaults to zero")
    @Default
    private @Meta int seaLevel = 0;
    @Value("ocean.palette")
    @Description("The palette to use for the ocean in this biome. Defaults to a blank palette.")
    @Default
    private @Meta Palette oceanPalette = new Palette() {
        @Override
        public BlockState get(int layer, double x, double y, double z, long seed) {
            return platform.getWorldHandle().air();
        }
    };
    @Value("carving.update-palette")
    @Default
    private @Meta boolean updatePalette = false;
    
    public BiomePaletteTemplate(Platform platform, SlantHolder.CalculationMethod slantCalculationMethod) {
        this.platform = platform;
        this.slantCalculationMethod = slantCalculationMethod;
    }
    
    @Override
    public BiomePaletteInfo get() {
        return new BiomePaletteInfo(PaletteHolder.of(palettes), SlantHolder.of(slantLayers, slantDepth, slantCalculationMethod),
                                    oceanPalette, seaLevel, updatePalette);
    }
}
