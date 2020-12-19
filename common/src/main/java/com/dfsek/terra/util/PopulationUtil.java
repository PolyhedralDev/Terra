package com.dfsek.terra.util;

import com.dfsek.terra.api.math.MathUtil;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.util.FastRandom;

public final class PopulationUtil {
    public static FastRandom getRandom(Chunk c) {
        return new FastRandom(MathUtil.getCarverChunkSeed(c.getX(), c.getZ(), c.getWorld().getSeed()));
    }
}
