package com.dfsek.terra.mod.util;

import com.dfsek.terra.minecraft.attribute.TerraBiomeIndex;


public final class BiomeUtil {
    private static volatile TerraBiomeIndex terraBiomes = new TerraBiomeIndex();

    private BiomeUtil() {
    }

    public static TerraBiomeIndex terraBiomes() {
        return terraBiomes;
    }

    public static void setTerraBiomes(TerraBiomeIndex biomes) {
        terraBiomes = biomes;
    }
}
