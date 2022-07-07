/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.image;

import net.jafama.FastMath;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


public class ImageBiomeProvider implements BiomeProvider {
    private final Map<Color, Biome> colorBiomeMap = new HashMap<>();
    private final BufferedImage image;
    private final int resolution;
    private final Align align;
    
    public ImageBiomeProvider(Set<Biome> registry, BufferedImage image, int resolution, Align align) {
        this.image = image;
        this.resolution = resolution;
        this.align = align;
        registry.forEach(biome -> colorBiomeMap.put(new Color(biome.getColor()), biome));
    }
    
    private static int distance(Color a, Color b) {
        return FastMath.abs(a.getRed() - b.getRed()) + FastMath.abs(a.getGreen() - b.getGreen()) + FastMath.abs(a.getBlue() - b.getBlue());
    }
    
    @Override
    public Biome getBiome(int x, int y, int z, long seed) {
        return getBiome(x, z);
    }
    
    public Biome getBiome(int x, int z) {
        x /= resolution;
        z /= resolution;
        Color color = align.getColor(image, x, z);
        return colorBiomeMap.get(colorBiomeMap.keySet()
                                              .stream()
                                              .reduce(colorBiomeMap.keySet().stream().findAny().orElseThrow(IllegalStateException::new),
                                                      (running, element) -> {
                                                          int d1 = distance(color, running);
                                                          int d2 = distance(color, element);
                                                          return d1 < d2 ? running : element;
                                                      }));
    }
    
    @Override
    public Optional<Biome> getBaseBiome(int x, int z, long seed) {
        return Optional.of(getBiome(x, z));
    }
    
    @Override
    public Iterable<Biome> getBiomes() {
        return colorBiomeMap.values();
    }
    
    public enum Align {
        CENTER {
            @Override
            public Color getColor(BufferedImage image, int x, int z) {
                return new Color(image.getRGB(FastMath.floorMod(x - image.getWidth() / 2, image.getWidth()),
                                              FastMath.floorMod(z - image.getHeight() / 2, image.getHeight())));
            }
        },
        NONE {
            @Override
            public Color getColor(BufferedImage image, int x, int z) {
                return new Color(image.getRGB(FastMath.floorMod(x, image.getWidth()), FastMath.floorMod(z, image.getHeight())));
            }
        };
        
        public abstract Color getColor(BufferedImage image, int x, int z);
    }
}
