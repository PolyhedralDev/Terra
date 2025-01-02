package com.dfsek.terra.minestom.api.filter;

public class ZFilter implements ChunkFilter {
    private final int z;

    public ZFilter(int z) { this.z = z; }

    @Override
    public boolean shouldPlaceTerrain(int x, int z) {
        return this.z == z;
    }

    @Override
    public boolean shouldPlaceFeatures(int x, int z) {
        return this.z == z;
    }
}
