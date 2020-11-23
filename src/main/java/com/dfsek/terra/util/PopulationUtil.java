package com.dfsek.terra.util;

import org.bukkit.Chunk;
import org.polydev.gaea.math.MathUtil;
import org.polydev.gaea.util.FastRandom;

public final class PopulationUtil {
    public static FastRandom getRandom(Chunk c) {
        return new FastRandom(MathUtil.getCarverChunkSeed(c.getX(), c.getZ(), c.getWorld().getSeed()));
    }
}
