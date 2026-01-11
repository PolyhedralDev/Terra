package com.dfsek.terra.addons.ore.utils;

import java.util.random.RandomGenerator;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.util.collection.BlockStateSet;
import com.dfsek.terra.api.world.WritableWorld;


public class VanillaOreUtils {
    private static boolean shouldExpose(RandomGenerator random, double exposedChance) {
        if(exposedChance >= 1.0F) return true;
        if(exposedChance <= 0.0F) return false;
        return random.nextFloat() < exposedChance;
    }

    public static boolean shouldPlace(BlockStateSet replaceable, BlockType type, Double exposedChance, RandomGenerator random,
                                      WritableWorld world,
                                      int x,
                                      int y, int z) {
        if(!replaceable.contains(type)) return false;
        if(shouldExpose(random, exposedChance)) return true; // Exposed blocks can be placed regardless of adjacency to air
        // Adjacency is checked after determining not exposed rather than vice-versa, assuming block checks are more expensive
        boolean adjacentAir = world.getBlockState(x, y, z - 1).air() ||
                              world.getBlockState(x, y, z + 1).air() ||
                              world.getBlockState(x, y - 1, z).air() ||
                              world.getBlockState(x, y + 1, z).air() ||
                              world.getBlockState(x - 1, y, z).air() ||
                              world.getBlockState(x + 1, y, z).air();
        return !adjacentAir; // Exposed check did not pass earlier so only blocks not adjacent air should place
    }
}
