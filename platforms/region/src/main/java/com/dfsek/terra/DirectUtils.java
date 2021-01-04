package com.dfsek.terra;

import net.querz.mca.MCAUtil;

public final class DirectUtils {

    /**
     * Compute long region ID from chunk coords
     *
     * @param x X
     * @param z Z
     * @return Region IS
     */
    public static long regionID(int x, int z) {
        return (((long) MCAUtil.chunkToRegion(x)) << 32) | (MCAUtil.chunkToRegion(z) & 0xffffffffL);
    }
}
