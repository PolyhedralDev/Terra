package com.dfsek.terra.addons.ore.utils;

import java.util.Random;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.util.collection.MaterialSet;
import com.dfsek.terra.api.world.WritableWorld;


public class VanillaOreUtils {
    private static boolean shouldExpose(Random random, double exposedChance) {
        if(exposedChance >= 1.0F) return true;
        if(exposedChance <= 0.0F) return false;
        return random.nextFloat() < exposedChance;
    }

    public static boolean shouldPlace(MaterialSet replaceable, BlockType type, Double exposedChance, Random random, WritableWorld world,
                                      int x,
                                      int y, int z) {
        if(!replaceable.contains(type)) return false;
        if(shouldExpose(random, exposedChance)) return true; // Exposed blocks can be placed regardless of adjacency to air
        // Adjacency is checked after determining not exposed rather than vice-versa, assuming block checks are more expensive
        boolean adjacentAir = world.getBlockState(x, y, z - 1).isAir() ||
                              world.getBlockState(x, y, z + 1).isAir() ||
                              world.getBlockState(x, y - 1, z).isAir() ||
                              world.getBlockState(x, y + 1, z).isAir() ||
                              world.getBlockState(x - 1, y, z).isAir() ||
                              world.getBlockState(x + 1, y, z).isAir();
        return !adjacentAir; // Exposed check did not pass earlier so only blocks not adjacent air should place
    }
}
