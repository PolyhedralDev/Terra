package com.dfsek.terra.api.util.world;

import com.dfsek.terra.api.math.MathUtil;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.util.FastRandom;

public final class PopulationUtil {
    public static FastRandom getRandom(Chunk c) {
        return getRandom(c, 0);
    }

    public static FastRandom getRandom(Chunk c, long salt) {
        return new FastRandom(MathUtil.getCarverChunkSeed(c.getX(), c.getZ(), c.getWorld().getSeed() + salt));
    }
}
