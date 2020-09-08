package com.dfsek.terra.biome.generators;

import org.bukkit.Material;
import org.polydev.gaea.biome.BiomeTerrain;
import org.polydev.gaea.math.FastNoise;
import org.polydev.gaea.world.BlockPalette;

public class MountainGenerator extends BiomeTerrain {
    private final BlockPalette p = new BlockPalette().add(Material.STONE, 1);
    public double getNoise(FastNoise fastNoise, int i, int i1) {
        return 0;
    }

    public double getNoise(FastNoise fastNoise, int x, int y, int z) {
        return (-Math.pow((float)y/96, 2))+1D + (fastNoise.getSimplexFractal(x, y, z)/2);
    }

    public BlockPalette getPalette() {
        return p;
    }
}
