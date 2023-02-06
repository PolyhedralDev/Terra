/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.image.v2;

import java.util.Optional;

import com.dfsek.terra.addons.image.converter.ColorConverter;
import com.dfsek.terra.addons.image.sampler.ColorSampler;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


public class ImageBiomeProvider implements BiomeProvider {
    private final int resolution;
    
    private final ColorConverter<Biome> colorConverter;
    
    private final ColorSampler colorSampler;
    
    public ImageBiomeProvider(ColorConverter<Biome> colorConverter, ColorSampler colorSampler, int resolution) {
        this.resolution = resolution;
        this.colorConverter = colorConverter;
        this.colorSampler = colorSampler;
    }
    
    @Override
    public Biome getBiome(int x, int y, int z, long seed) {
        return getBiome(x, z);
    }
    
    public Biome getBiome(int x, int z) {
        x /= resolution;
        z /= resolution;
        return colorConverter.apply(colorSampler.apply(x, z));
    }
    
    @Override
    public Optional<Biome> getBaseBiome(int x, int z, long seed) {
        return Optional.of(getBiome(x, z));
    }
    
    @Override
    public Iterable<Biome> getBiomes() {
        return colorConverter.getEntries();
    }
}
