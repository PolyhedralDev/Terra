package com.dfsek.terra.minestom.api.filter;

/**
 * This interface defines a filter for determining whether terrain and features
 * should be placed during chunk generation. Implementations of this interface
 * can apply custom logic to selectively control terrain and feature placement
 * in specific chunks for debugging purposes.
 */
public interface ChunkFilter {
    boolean shouldPlaceTerrain(int x, int z);
    boolean shouldPlaceFeatures(int x, int z);
}
