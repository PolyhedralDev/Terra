package com.dfsek.terra.config.loaders.config.biome.templates.provider;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.biome.provider.ImageBiomeProvider;
import com.dfsek.terra.config.builder.BiomeBuilder;

import java.awt.image.BufferedImage;
import java.util.stream.Collectors;

public class ImageProviderTemplate extends BiomeProviderTemplate {
    private final Registry<BiomeBuilder> biomes;
    @Value("image.name")
    private BufferedImage image;

    @Value("image.align")
    private ImageBiomeProvider.Align align;

    public ImageProviderTemplate(Registry<BiomeBuilder> set) {
        this.biomes = set;
    }

    @Override
    public BiomeProvider build(long seed) {
        return new ImageBiomeProvider(biomes.entries().stream().map(biomeBuilder -> biomeBuilder.apply(seed)).collect(Collectors.toSet()), image, resolution, align);
    }
}
