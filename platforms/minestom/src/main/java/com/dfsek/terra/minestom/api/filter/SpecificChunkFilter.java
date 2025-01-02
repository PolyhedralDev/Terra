package com.dfsek.terra.minestom.api.filter;

public class SpecificChunkFilter implements ChunkFilter {
    private final int x;
    private final int z;

    public SpecificChunkFilter(int x, int z) {
        this.x = x;
        this.z = z;
    }

    @Override
    public boolean shouldPlaceTerrain(int x, int z) {
        return this.x == x && this.z == z;
    }

    @Override
    public boolean shouldPlaceFeatures(int x, int z) {
        return this.x == x && this.z == z;
    }
}
