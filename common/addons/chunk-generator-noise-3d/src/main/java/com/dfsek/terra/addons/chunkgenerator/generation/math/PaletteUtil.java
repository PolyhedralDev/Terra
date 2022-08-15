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
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.chunk.generation.util.Palette;


public final class PaletteUtil {
    /**
     * Derivative constant.
     */
    private static final double DERIVATIVE_DIST = 0.55;
    
    public static Palette getPalette(int x, int y, int z, Sampler3D sampler, PaletteInfo paletteInfo, int depth) {
        SlantHolder slant = paletteInfo.slantHolder();
        if(!slant.isEmpty() && depth <= paletteInfo.maxSlantDepth()) {
            double dot = surfaceNormalDotProduct(sampler, x, y, z);
            if(dot <= slant.getMaxSlant()) {
                return slant.getPalette(dot).getPalette(y);
            }
        }
        
        return paletteInfo.paletteHolder().getPalette(y);
    }
    
    private static final Vector3[] samplePoints = {
            Vector3.of(0, 0, -DERIVATIVE_DIST),
            Vector3.of(0, 0, DERIVATIVE_DIST),
            Vector3.of(0, -DERIVATIVE_DIST, 0),
            Vector3.of(0, DERIVATIVE_DIST, 0),
            Vector3.of(-DERIVATIVE_DIST, 0, 0),
            Vector3.of(DERIVATIVE_DIST, 0, 0),
    };
    
    private static final Vector3 direction = Vector3.of(0, 1, 0);
    
    public static double surfaceNormalDotProduct(Sampler3D sampler, double x, double y, double z) {
        Vector3.Mutable surfaceNormalApproximation = Vector3.Mutable.of(0, 0, 0);
        for(Vector3 point : samplePoints) {
            double scalar = -sampler.sample(x+point.getX(), y+point.getY(), z+point.getZ());
            surfaceNormalApproximation.add(point.mutable().multiply(scalar));
        }
        return direction.dot(surfaceNormalApproximation.normalize());
    }
}