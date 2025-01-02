package com.dfsek.terra.minestom.api.filter;

public class XFilter implements ChunkFilter {
    private final int x;

    public XFilter(int x) { this.x = x; }

    @Override
    public boolean shouldPlaceTerrain(int x, int z) {
        return this.x == x;
    }

    @Override
    public boolean shouldPlaceFeatures(int x, int z) {
        return this.x == x;
    }
}
