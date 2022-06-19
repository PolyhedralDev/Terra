/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.chunkgenerator.generation.math.samplers;

import net.jafama.FastMath;

import com.dfsek.terra.addons.chunkgenerator.config.noise.BiomeNoiseProperties;
import com.dfsek.terra.addons.chunkgenerator.generation.math.interpolation.ChunkInterpolator;
import com.dfsek.terra.addons.chunkgenerator.generation.math.interpolation.ElevationInterpolator;
import com.dfsek.terra.api.properties.PropertyKey;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


public class Sampler3D {
    private final ChunkInterpolator interpolator;
    private final ElevationInterpolator elevationInterpolator;
    
    public Sampler3D(int x, int z, long seed, int minHeight, int maxHeight, BiomeProvider provider, int elevationSmooth,
                     PropertyKey<BiomeNoiseProperties> noisePropertiesKey, int maxBlend) {
        this.interpolator = new ChunkInterpolator(seed, x, z, provider,
                                                  minHeight, maxHeight, noisePropertiesKey, maxBlend);
        this.elevationInterpolator = new ElevationInterpolator(seed, x, z, provider, elevationSmooth, noisePropertiesKey);
    }
    
    public double sample(double x, double y, double z) {
        return interpolator.getNoise(x, y, z) + elevationInterpolator.getElevation(FastMath.roundToInt(x), FastMath.roundToInt(z));
    }
    
    public double sample(int x, int y, int z) {
        return interpolator.getNoise(x, y, z) + elevationInterpolator.getElevation(FastMath.roundToInt(x), FastMath.roundToInt(z));
    }
}
