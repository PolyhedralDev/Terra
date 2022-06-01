/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.chunkgenerator.generation.math.interpolation;

/**
 * Class for bilinear interpolation of values arranged on a unit square.
 */
public class Interpolator3 {
    private final Interpolator bottom;
    private final Interpolator top;
    
    /**
     * Constructs an interpolator with given values as vertices of a unit cube.
     * * @param _000 The value at <code>(t, u, v) = (0, 0, 0)</code>.
     * * @param _100 The value at <code>(t, u, v) = (1, 0, 0)</code>.
     * * @param _010 The value at <code>(t, u, v) = (0, 1, 0)</code>.
     * * @param _110 The value at <code>(t, u, v) = (1, 1, 0)</code>.
     * * @param _001 The value at <code>(t, u, v) = (0, 0, 1)</code>.
     * * @param _101 The value at <code>(t, u, v) = (1, 0, 1)</code>.
     * * @param _011 The value at <code>(t, u, v) = (0, 1, 1)</code>.
     * * @param _111 The value at <code>(t, u, v) = (1, 1, 1)</code>.
     */
    public Interpolator3(double _000, double _100,
                         double _010, double _110,
                         double _001, double _101,
                         double _011, double _111) {
        this.top = new Interpolator(_000, _010, _001, _011);
        this.bottom = new Interpolator(_100, _110, _101, _111);
    }
    
    public double trilerp(double x, double y, double z) {
        return Interpolator.lerp(x, top.bilerp(y, z), bottom.bilerp(y, z));
    }
}