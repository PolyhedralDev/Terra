package com.dfsek.terra.api.world.biome.provider;

import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.registry.OpenRegistry;
import net.jafama.FastMath;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class ImageBiomeProvider implements BiomeProvider, BiomeProvider.BiomeProviderBuilder { // This provider does not need a seed, so it is its own builder.
    private final Map<Color, TerraBiome> colorBiomeMap = new HashMap<>();
    private final BufferedImage image;
    private final int resolution;
    private final Align align;

    public ImageBiomeProvider(Registry<TerraBiome> registry, BufferedImage image, int resolution, Align align) {
        this.image = image;
        this.resolution = resolution;
        this.align = align;
        registry.forEach(biome -> colorBiomeMap.put(new Color(biome.getColor()), biome));
    }

    private static int distance(Color a, Color b) {
        return FastMath.abs(a.getRed() - b.getRed()) + FastMath.abs(a.getGreen() - b.getGreen()) + FastMath.abs(a.getBlue() - b.getBlue());
    }

    @Override
    public TerraBiome getBiome(int x, int z) {
        x /= resolution;
        z /= resolution;
        Color color = align.getColor(image, x, z);
        return colorBiomeMap.get(colorBiomeMap.keySet().stream().reduce(colorBiomeMap.keySet().stream().findAny().orElseThrow(IllegalStateException::new), (running, element) -> {
            int d1 = distance(color, running);
            int d2 = distance(color, element);
            return d1 < d2 ? running : element;
        }));
    }

    @Override
    public BiomeProvider build(long seed) {
        return this;
    }

    public enum Align {
        CENTER {
            @Override
            public Color getColor(BufferedImage image, int x, int z) {
                return new Color(image.getRGB(FastMath.floorMod(x - image.getWidth() / 2, image.getWidth()), FastMath.floorMod(z - image.getHeight() / 2, image.getHeight())));
            }
        }, NONE {
            @Override
            public Color getColor(BufferedImage image, int x, int z) {
                return new Color(image.getRGB(FastMath.floorMod(x, image.getWidth()), FastMath.floorMod(z, image.getHeight())));
            }
        };

        public abstract Color getColor(BufferedImage image, int x, int z);
    }
}
