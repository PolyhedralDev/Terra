/*
 * Copyright (c) 2020-2025 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.chunkgenerator.generation.math.interpolation;


import com.dfsek.seismic.math.numericanalysis.interpolation.InterpolationFunctions;


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

    //TODO this system is not very good, replace it wholesale

    /**
     * 2D Bilinear interpolation between 4 points on a unit square.
     *
     * @param s - X value
     * @param t - Z value
     *
     * @return double - The interpolated value.
     */
    public double bilerp(double s, double t) {
        double v01 = InterpolationFunctions.lerp(v0, v1, s);
        double v23 = InterpolationFunctions.lerp(v2, v3, s);
        return InterpolationFunctions.lerp(v01, v23, t);
    }
}