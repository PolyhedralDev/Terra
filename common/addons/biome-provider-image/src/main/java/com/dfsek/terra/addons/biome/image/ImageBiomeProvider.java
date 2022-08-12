/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.image;

import java.awt.image.BufferedImage;
import java.util.Optional;

import com.dfsek.terra.addons.image.converter.ColorConverter;
import com.dfsek.terra.addons.image.picker.ColorPicker;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


public class ImageBiomeProvider implements BiomeProvider {
    private final BufferedImage image;
    private final int resolution;
    
    private final ColorConverter<Biome> colorConverter;
    
    private final ColorPicker colorPicker;
    
    public ImageBiomeProvider(BufferedImage image, ColorConverter<Biome> colorConverter, ColorPicker colorPicker, int resolution) {
        this.image = image;
        this.resolution = resolution;
        this.colorConverter = colorConverter;
        this.colorPicker = colorPicker;
    }
    
    @Override
    public Biome getBiome(int x, int y, int z, long seed) {
        return getBiome(x, z);
    }
    
    public Biome getBiome(int x, int z) {
        x /= resolution;
        z /= resolution;
        return colorConverter.apply(colorPicker.apply(image, x, z));
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
