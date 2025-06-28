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
public class Interpolator3 {
    private final double _000;
    private final double _001;
    private final double _010;
    private final double _011;
    private final double _100;
    private final double _101;
    private final double _110;
    private final double _111;

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
        this._000 = _000;
        this._001 = _001;
        this._010 = _010;
        this._011 = _011;
        this._100 = _100;
        this._101 = _101;
        this._110 = _110;
        this._111 = _111;
    }

    public double trilerp(double x, double y, double z) {
        return InterpolationFunctions.triLerp(
            _000, _001, _010, _011,
            _100, _101, _110, _111,
            x, y, z
        );
    }
}