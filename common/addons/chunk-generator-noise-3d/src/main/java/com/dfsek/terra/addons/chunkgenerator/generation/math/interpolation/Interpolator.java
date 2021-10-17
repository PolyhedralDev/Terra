package com.dfsek.terra.addons.chunkgenerator.generation.math.interpolation;

/**
 * Class for bilinear interpolation of values arranged on a unit square.
 */
public class Interpolator {
    private final double v0, v1, v2, v3;
    
    /**
     * Constructs an interpolator with given values as vertices of a unit square.
     *
     * @param v0 - (0,0)
     * @param v1 - (1,0)
     * @param v2 - (0,1)
     * @param v3 - (1,1)
     */
    public Interpolator(double v0, double v1, double v2, double v3) {
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }
    
    /**
     * 1D Linear interpolation between 2 points 1 unit apart.
     *
     * @param t  - Distance from v0. Total distance between v0 and v1 is 1 unit.
     * @param v0 - Value at v0.
     * @param v1 - Value at v1.
     *
     * @return double - The interpolated value.
     */
    public static double lerp(double t, double v0, double v1) {
        return v0 + t * (v1 - v0);
    }
    
    /**
     * 2D Bilinear interpolation between 4 points on a unit square.
     *
     * @param s - X value
     * @param t - Z value
     *
     * @return double - The interpolated value.
     */
    public double bilerp(double s, double t) {
        double v01 = lerp(s, v0, v1);
        double v23 = lerp(s, v2, v3);
        return lerp(t, v01, v23);
    }
}