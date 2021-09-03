package com.dfsek.terra;

import com.dfsek.terra.api.util.MathUtil;

import net.querz.mca.MCAUtil;


public final class DirectUtils {
    
    /**
     * Compute long region ID from chunk coords
     *
     * @param x X
     * @param z Z
     *
     * @return Region IS
     */
    public static long regionID(int x, int z) {
        return MathUtil.squash(MCAUtil.chunkToRegion(x), MCAUtil.chunkToRegion(z));
    }
}
