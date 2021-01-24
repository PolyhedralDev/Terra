package com.dfsek.terra.generation.math.interpolation;

public interface ChunkInterpolator {
    /**
     * Gets the noise at a pair of internal chunk coordinates.
     *
     * @param x The internal X coordinate (0-15).
     * @param z The internal Z coordinate (0-15).
     * @return double - The interpolated noise at the coordinates.
     */
    double getNoise(double x, double y, double z);
}
