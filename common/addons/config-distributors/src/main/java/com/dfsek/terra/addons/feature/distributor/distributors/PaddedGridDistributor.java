package com.dfsek.terra.addons.feature.distributor.distributors;

import com.dfsek.seismic.algorithms.hashing.HashingFunctions;
import com.dfsek.seismic.math.integer.IntegerFunctions;
import com.dfsek.terra.api.structure.feature.Distributor;

public class PaddedGridDistributor implements Distributor {
    private final int width;
    private final int cellWidth;
    private final int salt;

    public PaddedGridDistributor(int width, int padding, int salt) {
        this.width = width;
        this.salt = salt;
        this.cellWidth = width + padding;
    }

    @Override
    public boolean matches(int x, int z, long seed) {
        int cellX = Math.floorDiv(x, cellWidth);
        int cellZ = Math.floorDiv(z, cellWidth);

        int localX = x - (cellX * cellWidth);
        int localZ = z - (cellZ * cellWidth);

        if (localX >= width || localZ >= width) {
            return false;
        }

        long hash = HashingFunctions.murmur64(IntegerFunctions.squash(cellX, cellZ)) ^ seed;
        hash += salt;

        hash = HashingFunctions.splitMix64(hash);
        int targetX = (int) ((hash & 0x7FFFFFFFFFFFFFFFL) % width);

        if (localX != targetX) {
            return false;
        }

        hash = HashingFunctions.splitMix64(hash);
        int targetZ = (int) ((hash & 0x7FFFFFFFFFFFFFFFL) % width);

        return localZ == targetZ;
    }
}
