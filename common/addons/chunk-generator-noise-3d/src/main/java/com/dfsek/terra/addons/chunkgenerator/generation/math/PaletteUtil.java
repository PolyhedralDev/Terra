/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.chunkgenerator.generation.math;

import com.dfsek.terra.addons.chunkgenerator.config.palette.PaletteInfo;
import com.dfsek.terra.addons.chunkgenerator.generation.math.samplers.Sampler3D;
import com.dfsek.terra.addons.chunkgenerator.palette.SlantHolder;
import com.dfsek.terra.api.world.chunk.generation.util.Palette;


public final class PaletteUtil {
    /**
     * Derivative constant.
     */
    private static final double DERIVATIVE_DIST = 0.55;
    
    public static Palette getPalette(int x, int y, int z, Sampler3D sampler, PaletteInfo paletteInfo, int depth) {
        SlantHolder slant = paletteInfo.slantHolder();
        if(!slant.isEmpty() && depth <= paletteInfo.maxSlantDepth()) {
            double slope = derivative(sampler, x, y, z);
            if(slope > slant.getMinSlope()) {
                return slant.getPalette(slope).getPalette(y);
            }
        }
        
        return paletteInfo.paletteHolder().getPalette(y);
    }
    
    public static double derivative(Sampler3D sampler, double x, double y, double z) {
        double baseSample = sampler.sample(x, y, z);
        
        double xVal1 = (sampler.sample(x + DERIVATIVE_DIST, y, z) - baseSample) / DERIVATIVE_DIST;
        double xVal2 = (sampler.sample(x - DERIVATIVE_DIST, y, z) - baseSample) / DERIVATIVE_DIST;
        double zVal1 = (sampler.sample(x, y, z + DERIVATIVE_DIST) - baseSample) / DERIVATIVE_DIST;
        double zVal2 = (sampler.sample(x, y, z - DERIVATIVE_DIST) - baseSample) / DERIVATIVE_DIST;
        double yVal1 = (sampler.sample(x, y + DERIVATIVE_DIST, z) - baseSample) / DERIVATIVE_DIST;
        double yVal2 = (sampler.sample(x, y - DERIVATIVE_DIST, z) - baseSample) / DERIVATIVE_DIST;
        
        return Math.sqrt(((xVal2 - xVal1) * (xVal2 - xVal1)) + ((zVal2 - zVal1) * (zVal2 - zVal1)) + ((yVal2 - yVal1) * (yVal2 - yVal1)));
    }
}