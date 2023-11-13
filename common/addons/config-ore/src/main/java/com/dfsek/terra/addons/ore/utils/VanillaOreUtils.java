package com.dfsek.terra.addons.ore.utils;

import java.util.Random;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.util.collection.MaterialSet;
import com.dfsek.terra.api.world.WritableWorld;


public class VanillaOreUtils {
    protected static boolean shouldNotDiscard(Random random, double chance) {
        if(chance <= 0.0F) {
            return true;
        } else if(chance >= 1.0F) {
            return false;
        } else {
            return random.nextFloat() >= chance;
        }
    }

    public static boolean shouldPlace(MaterialSet replaceable, BlockType type, Double exposed, Random random, WritableWorld world, int x,
                                      int y, int z) {
        if(!replaceable.contains(type)) {
            return false;
        } else if(shouldNotDiscard(random, exposed)) {
            return true;
        } else {
            return !(world.getBlockState(x, y, z - 1).isAir() ||
                     world.getBlockState(x, y, z + 1).isAir() ||
                     world.getBlockState(x, y - 1, z).isAir() ||
                     world.getBlockState(x, y + 1, z).isAir() ||
                     world.getBlockState(x - 1, y, z).isAir() ||
                     world.getBlockState(x + 1, y, z).isAir());
        }
    }

}

