/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.image;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Description;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import java.awt.image.BufferedImage;
import java.util.HashSet;

import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


@SuppressWarnings("FieldMayBeFinal")
public class ImageProviderTemplate implements ObjectTemplate<BiomeProvider> {
    private final Registry<Biome> biomes;
    @Value("resolution")
    @Default
    @Description("Sets the resolution at which to sample the image.")
    private int resolution = 1;
    @Value("image.name")
    @Description("Sets the location of the image on the filesystem, relative to the pack root.")
    private BufferedImage image;
    @Value("image.align")
    @Description("Sets the alignment style to use for the image.")
    private ImageBiomeProvider.Align align;
    
    public ImageProviderTemplate(Registry<Biome> set) {
        this.biomes = set;
    }
    
    @Override
    public BiomeProvider get() {
        return new ImageBiomeProvider(new HashSet<>(biomes.entries()), image, resolution, align);
    }
}
