package com.dfsek.terra.minestom.api.filter;

public class NoTerrainFilter implements ChunkFilter {
    @Override
    public boolean shouldPlaceTerrain(int x, int z) {
        return false;
    }

    @Override
    public boolean shouldPlaceFeatures(int x, int z) {
        return true;
    }
}
