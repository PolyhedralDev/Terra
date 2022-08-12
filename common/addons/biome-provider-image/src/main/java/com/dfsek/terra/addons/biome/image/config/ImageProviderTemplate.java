/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.image.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Description;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import java.awt.image.BufferedImage;

import com.dfsek.terra.addons.biome.image.ImageBiomeProvider;
import com.dfsek.terra.addons.image.converter.ColorConverter;
import com.dfsek.terra.addons.image.picker.ColorPicker;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


@SuppressWarnings("FieldMayBeFinal")
public class ImageProviderTemplate implements ObjectTemplate<BiomeProvider> {
    
    @Value("resolution")
    @Default
    @Description("Sets the resolution at which to sample the image.")
    private int resolution = 1;
    
    @Value("image")
    private BufferedImage image;
    
    @Value("mode")
    private ColorPicker colorPicker;
    
    @Value("biomes")
    private ColorConverter<Biome> colorConverter;
    
    @Override
    public BiomeProvider get() {
        return new ImageBiomeProvider(image, colorConverter, colorPicker, resolution);
    }
}
