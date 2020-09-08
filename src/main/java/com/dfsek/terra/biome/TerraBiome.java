package com.dfsek.terra.biome;

import com.dfsek.terra.biome.generators.MountainGenerator;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.biome.BiomeTerrain;
import org.polydev.gaea.biome.Decorator;
import org.polydev.gaea.structures.features.Feature;

import java.util.List;

public enum TerraBiome implements Biome {
    PLAINS(null), MOUNTAINS(new MountainGenerator());

    private BiomeTerrain t;
    TerraBiome(BiomeTerrain t) {
        this.t = t;
    }

    public org.bukkit.block.Biome getVanillaBiome() {
        return org.bukkit.block.Biome.PLAINS;
    }

    public BiomeTerrain getGenerator() {
        return t;
    }

    public List<Feature> getStructureFeatures() {
        return null;
    }

    public Decorator getDecorator() {
        return null;
    }
}
