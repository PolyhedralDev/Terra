package com.dfsek.terra.biome.provider;

import com.dfsek.terra.biome.TerraBiome;
import com.dfsek.terra.registry.TerraRegistry;
import net.jafama.FastMath;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class ImageBiomeProvider implements BiomeProvider {
    private final Map<Color, TerraBiome> colorBiomeMap = new HashMap<>();
    private final BufferedImage image;
    private final int resolution;

    public ImageBiomeProvider(TerraRegistry<TerraBiome> registry, BufferedImage image, int resolution) {
        this.image = image;
        this.resolution = resolution;
        registry.forEach(biome -> colorBiomeMap.put(new Color(biome.getColor()), biome));
    }

    private static int distance(Color a, Color b) {
        return FastMath.abs(a.getRed() - b.getRed()) + FastMath.abs(a.getGreen() - b.getGreen()) + FastMath.abs(a.getBlue() - b.getBlue());
    }

    @Override
    public TerraBiome getBiome(int x, int z) {
        Color color = new Color(image.getRGB(FastMath.floorMod(x / resolution, image.getWidth()), FastMath.floorMod(z / resolution, image.getHeight())));
        return colorBiomeMap.get(colorBiomeMap.keySet().stream().reduce(colorBiomeMap.keySet().stream().findAny().orElseThrow(IllegalStateException::new), (running, element) -> {
            int d1 = distance(color, running);
            int d2 = distance(color, element);
            return d1 < d2 ? running : element;
        }));
    }

    public static class ImageBiomeProviderBuilder implements BiomeProviderBuilder {
        private final BufferedImage image;
        private final int resolution;
        private final TerraRegistry<TerraBiome> registry;

        public ImageBiomeProviderBuilder(BufferedImage image, int resolution, TerraRegistry<TerraBiome> registry) {
            this.image = image;
            this.resolution = resolution;
            this.registry = registry;
        }

        @Override
        public BiomeProvider build(long seed) {
            return new ImageBiomeProvider(registry, image, resolution);
        }
    }
}
