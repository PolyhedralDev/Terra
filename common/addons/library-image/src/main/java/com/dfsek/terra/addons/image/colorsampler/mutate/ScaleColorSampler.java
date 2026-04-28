package com.dfsek.terra.addons.image.colorsampler.mutate;

import com.dfsek.terra.addons.image.colorsampler.ColorSampler;


public class ScaleColorSampler implements ColorSampler {

    private final ColorSampler sampler;
    private final double scaleX, scaleZ;
    private final InterpolationMethod method;


    public ScaleColorSampler(ColorSampler sampler, double scaleX, double scaleZ, String method) {
        this.sampler = sampler;
        this.scaleX = scaleX;
        this.scaleZ = scaleZ;
        switch(method.toLowerCase()){
            case "nearest":
                this.method = InterpolationMethod.NEAREST;
                break;
            case "bilinear":
                this.method = InterpolationMethod.BILINEAR;
                break;
            case "bicubic":
                this.method = InterpolationMethod.BICUBIC;
                break;
            default:
                throw new IllegalArgumentException("Unknown interpolation method: " + method);
        }
    }

    @Override
    public int apply(int x, int z) {
        double sx = x / scaleX;
        double sz = z / scaleZ;

        switch (method) {
            case BILINEAR:
                return applyBilinear(sx, sz);
            case BICUBIC:
                return applyBicubic(sx, sz);
            case NEAREST:
            default:
                return sampler.apply((int) sx, (int) sz);
        }
    }

    private int applyBilinear(double x, double z) {
        int x0 = (int) Math.floor(x);
        int z0 = (int) Math.floor(z);
        double tx = x - x0;
        double tz = z - z0;

        int c00 = sampler.apply(x0, z0);
        int c10 = sampler.apply(x0 + 1, z0);
        int c01 = sampler.apply(x0, z0 + 1);
        int c11 = sampler.apply(x0 + 1, z0 + 1);

        return interpolateFourColors(c00, c10, c01, c11, tx, tz);
    }

    private int applyBicubic(double x, double z) {
        int x0 = (int) Math.floor(x);
        int z0 = (int) Math.floor(z);
        double tx = x - x0;
        double tz = z - z0;

        int[] colResults = new int[4];

        for (int i = -1; i <= 2; i++) {
            int c0 = sampler.apply(x0 - 1, z0 + i);
            int c1 = sampler.apply(x0,     z0 + i);
            int c2 = sampler.apply(x0 + 1, z0 + i);
            int c3 = sampler.apply(x0 + 2, z0 + i);
            colResults[i + 1] = cubicInterpolateRow(c0, c1, c2, c3, tx);
        }

        return cubicInterpolateRow(colResults[0], colResults[1], colResults[2], colResults[3], tz);
    }

    private int interpolateFourColors(int c00, int c10, int c01, int c11, double tx, double tz) {
        int a = (int) lerp(lerp(getA(c00), getA(c10), tx), lerp(getA(c01), getA(c11), tx), tz);
        int r = (int) lerp(lerp(getR(c00), getR(c10), tx), lerp(getR(c01), getR(c11), tx), tz);
        int g = (int) lerp(lerp(getG(c00), getG(c10), tx), lerp(getG(c01), getG(c11), tx), tz);
        int b = (int) lerp(lerp(getB(c00), getB(c10), tx), lerp(getB(c01), getB(c11), tx), tz);
        return argb(a, r, g, b);
    }

    private int cubicInterpolateRow(int c0, int c1, int c2, int c3, double t) {
        int a = clamp(cubic(getA(c0), getA(c1), getA(c2), getA(c3), t));
        int r = clamp(cubic(getR(c0), getR(c1), getR(c2), getR(c3), t));
        int g = clamp(cubic(getG(c0), getG(c1), getG(c2), getG(c3), t));
        int b = clamp(cubic(getB(c0), getB(c1), getB(c2), getB(c3), t));
        return argb(a, r, g, b);
    }

    private double lerp(double start, double end, double t) {
        return start + t * (end - start);
    }

    private double cubic(double p0, double p1, double p2, double p3, double t) {
        return 0.5 * ((2 * p1) + (-p0 + p2) * t + (2 * p0 - 5 * p1 + 4 * p2 - p3) * t * t + (-p0 + 3 * p1 - 3 * p2 + p3) * t * t * t);
    }

    private int getA(int c) { return (c >> 24) & 0xFF; }
    private int getR(int c) { return (c >> 16) & 0xFF; }
    private int getG(int c) { return (c >> 8) & 0xFF; }
    private int getB(int c) { return c & 0xFF; }

    private int argb(int a, int r, int g, int b) {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private int clamp(double val) {
        return Math.max(0, Math.min(255, (int) val));
    }

    private enum InterpolationMethod {
        NEAREST,
        BILINEAR,
        BICUBIC,
    }
}
