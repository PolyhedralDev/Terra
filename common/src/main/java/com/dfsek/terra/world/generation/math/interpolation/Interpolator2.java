package com.dfsek.terra.world.generation.math.interpolation;

public interface Interpolator2 {
    /**
     * 2D Bilinear interpolation between 4 points on a unit square.
     *
     * @param s - X value
     * @param t - Z value
     * @return double - The interpolated value.
     */
    double interpolate(double s, double t);
}
