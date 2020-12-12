package com.dfsek.terra.util;

import com.dfsek.terra.api.gaea.math.MathUtil;
import com.dfsek.terra.api.gaea.util.FastRandom;
import com.dfsek.terra.api.generic.world.Chunk;

public final class PopulationUtil {
    public static FastRandom getRandom(Chunk c) {
        return new FastRandom(MathUtil.getCarverChunkSeed(c.getX(), c.getZ(), c.getWorld().getSeed()));
    }
}
