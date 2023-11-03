/*
 * Copyright (c) 2020-2023 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.chunkgenerator.generation.math;

import com.dfsek.terra.addons.chunkgenerator.generation.math.samplers.Sampler3D;
import com.dfsek.terra.addons.chunkgenerator.palette.BiomePaletteInfo;
import com.dfsek.terra.addons.chunkgenerator.palette.slant.SlantHolder;
import com.dfsek.terra.api.world.chunk.generation.util.Palette;


public final class PaletteUtil {
    
    public static Palette getPalette(int x, int y, int z, Sampler3D sampler, BiomePaletteInfo paletteInfo, int depth) {
        SlantHolder slantHolder = paletteInfo.slantHolder();
        if(slantHolder.isAboveDepth(depth)) {
            double slant = slantHolder.calculateSlant(sampler, x, y, z);
            if(slantHolder.isInSlantThreshold(slant)) {
                return slantHolder.getPalette(slant).getPalette(y);
            }
        }
        
        return paletteInfo.paletteHolder().getPalette(y);
    }
}