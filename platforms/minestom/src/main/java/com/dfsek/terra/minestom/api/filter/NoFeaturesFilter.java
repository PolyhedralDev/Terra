package com.dfsek.terra.minestom.api.filter;

public class NoFeaturesFilter implements ChunkFilter {
    @Override
    public boolean shouldPlaceTerrain(int x, int z) {
        return true;
    }

    @Override
    public boolean shouldPlaceFeatures(int x, int z) {
        return false;
    }
}
