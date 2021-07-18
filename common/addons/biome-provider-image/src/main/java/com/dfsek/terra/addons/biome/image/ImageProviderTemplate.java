package com.dfsek.terra.addons.biome.image;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.util.seeded.SeededTerraBiome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;

import java.awt.image.BufferedImage;
import java.util.stream.Collectors;

public class ImageProviderTemplate /*extends BiomeProviderTemplate */ {
    private final Registry<SeededTerraBiome> biomes;
    @Value("image.name")
    private BufferedImage image;

    @Value("image.align")
    private ImageBiomeProvider.Align align;

    private int resolution;

    public ImageProviderTemplate(Registry<SeededTerraBiome> set) {
        this.biomes = set;
    }

    //@Override
    public BiomeProvider build(long seed) {
        return new ImageBiomeProvider(biomes.entries().stream().map(biomeBuilder -> biomeBuilder.build(seed)).collect(Collectors.toSet()), image, resolution, align);
    }
}
