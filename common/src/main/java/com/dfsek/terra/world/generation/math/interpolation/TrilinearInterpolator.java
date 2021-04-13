package com.dfsek.terra.world.generation.math.interpolation;

/**
 * Class for bilinear interpolation of values arranged on a unit square.
 */
public class TrilinearInterpolator implements Interpolator3 {
    private final BilinearInterpolator bottom;
    private final BilinearInterpolator top;
    private final double length, width, height;

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
    public TrilinearInterpolator(double _000, double _100,
                                 double _010, double _110,
                                 double _001, double _101,
                                 double _011, double _111, double length, double width, double height) {
        this.length = length;
        this.width = width;
        this.height = height;
        this.top = new BilinearInterpolator(_000, _010, _001, _011, 1, 1);
        this.bottom = new BilinearInterpolator(_100, _110, _101, _111, 1, 1);
    }

    @Override
    public double interpolate(double x, double y, double z) {
        x/=length;
        z/=width;
        y/=height;
        return BilinearInterpolator.lerp(x, top.interpolate(y, z), bottom.interpolate(y, z));
    }
}