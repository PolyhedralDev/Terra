package com.dfsek.terra.config.loaders.config.biome.templates.source;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.biome.provider.BiomeProvider;
import com.dfsek.terra.api.world.biome.provider.ImageBiomeProvider;
import com.dfsek.terra.registry.config.BiomeRegistry;

import java.awt.image.BufferedImage;

public class ImageProviderTemplate extends BiomeProviderTemplate {
    @Value("image.name")
    private BufferedImage image;

    @Value("image.align")
    private ImageBiomeProvider.Align align;

    public ImageProviderTemplate(Registry<TerraBiome> registry) {
        super(registry);
    }

    @Override
    public BiomeProvider build(long seed) {
        return new ImageBiomeProvider(registry, image, resolution, align);
    }
}
