/*
 * Copyright (c) 2020-2023 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.chunkgenerator.generation.math.interpolation;

import com.dfsek.terra.api.util.MathUtil;


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
     * 2D Bilinear interpolation between 4 points on a unit square.
     *
     * @param s - X value
     * @param t - Z value
     *
     * @return double - The interpolated value.
     */
    public double bilerp(double s, double t) {
        double v01 = MathUtil.lerp(s, v0, v1);
        double v23 = MathUtil.lerp(s, v2, v3);
        return MathUtil.lerp(t, v01, v23);
    }
}