package com.dfsek.terra.config.loaders.config.biome.templates.source;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.biome.provider.BiomeProvider;
import com.dfsek.terra.biome.provider.ImageBiomeProvider;
import com.dfsek.terra.registry.BiomeRegistry;

import java.awt.image.BufferedImage;

public class ImageProviderTemplate extends BiomeProviderTemplate {
    @Value("image.name")
    private BufferedImage image;

    @Value("image.align")
    private ImageBiomeProvider.Align align;

    public ImageProviderTemplate(BiomeRegistry registry) {
        super(registry);
    }

    @Override
    public BiomeProvider build(long seed) {
        return new ImageBiomeProvider(registry, image, resolution, align);
    }
}
