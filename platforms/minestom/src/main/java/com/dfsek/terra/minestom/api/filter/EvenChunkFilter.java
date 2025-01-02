package com.dfsek.terra.minestom.api.filter;

public class EvenChunkFilter implements ChunkFilter {
    @Override
    public boolean shouldPlaceTerrain(int x, int z) {
        return x % 2 == 0 && z % 2 == 0;
    }

    @Override
    public boolean shouldPlaceFeatures(int x, int z) {
        return x % 2 == 0 && z % 2 == 0;
    }
}
