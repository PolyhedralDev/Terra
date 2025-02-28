/*
 * Copyright (c) 2020-2025 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.samplers.noise.simplex;

/**
 * NoiseSampler implementation to provide OpenSimplex2 (Smooth Variant) noise.
 */
public class OpenSimplex2SSampler extends SimplexStyleSampler {
    @Override
    @SuppressWarnings("NumericOverflow")
    public double getNoiseRaw(long sl, double x, double y) {
        int seed = (int) sl;
        // 2D OpenSimplex2S case is a modified 2D simplex noise.

        final double SQRT3 = 1.7320508075688772935274463415059;
        final double G2 = (3 - SQRT3) / 6;

        final double F2 = 0.5f * (SQRT3 - 1);
        double s = (x + y) * F2;
        x += s;
        y += s;


        int i = (int) Math.floor(x);
        int j = (int) Math.floor(y);
        double xi = x - i;
        double yi = y - j;

        i *= PRIME_X;
        j *= PRIME_Y;
        int i1 = i + PRIME_X;
        int j1 = j + PRIME_Y;

        double t = (xi + yi) * G2;
        double x0 = xi - t;
        double y0 = yi - t;

        double a0 = (2.0 / 3.0) - x0 * x0 - y0 * y0;
        double value = (a0 * a0) * (a0 * a0) * gradCoord(seed, i, j, x0, y0);

        double a1 = 2 * (1 - 2 * G2) * (1 / G2 - 2) * t + ((-2 * (1 - 2 * G2) * (1 - 2 * G2)) + a0);
        double x1 = x0 - (1 - 2 * G2);
        double y1 = y0 - (1 - 2 * G2);
        value += (a1 * a1) * (a1 * a1) * gradCoord(seed, i1, j1, x1, y1);

        // Nested conditionals were faster than compact bit logic/arithmetic.
        double xmyi = xi - yi;
        if(t > G2) {
            if(xi + xmyi > 1) {
                double x2 = x0 + (3 * G2 - 2);
                double y2 = y0 + (3 * G2 - 1);
                double a2 = (2.0 / 3.0) - x2 * x2 - y2 * y2;
                if(a2 > 0) {
                    value += (a2 * a2) * (a2 * a2) * gradCoord(seed, i + (PRIME_X << 1), j + PRIME_Y, x2, y2);
                }
            } else {
                double x2 = x0 + G2;
                double y2 = y0 + (G2 - 1);
                double a2 = (2.0 / 3.0) - x2 * x2 - y2 * y2;
                if(a2 > 0) {
                    value += (a2 * a2) * (a2 * a2) * gradCoord(seed, i, j + PRIME_Y, x2, y2);
                }
            }

            if(yi - xmyi > 1) {
                double x3 = x0 + (3 * G2 - 1);
                double y3 = y0 + (3 * G2 - 2);
                double a3 = (2.0 / 3.0) - x3 * x3 - y3 * y3;
                if(a3 > 0) {
                    value += (a3 * a3) * (a3 * a3) * gradCoord(seed, i + PRIME_X, j + (PRIME_Y << 1), x3, y3);
                }
            } else {
                double x3 = x0 + (G2 - 1);
                double y3 = y0 + G2;
                double a3 = (2.0 / 3.0) - x3 * x3 - y3 * y3;
                if(a3 > 0) {
                    value += (a3 * a3) * (a3 * a3) * gradCoord(seed, i + PRIME_X, j, x3, y3);
                }
            }
        } else {
            if(xi + xmyi < 0) {
                double x2 = x0 + (1 - G2);
                double y2 = y0 - G2;
                double a2 = (2.0 / 3.0) - x2 * x2 - y2 * y2;
                if(a2 > 0) {
                    value += (a2 * a2) * (a2 * a2) * gradCoord(seed, i - PRIME_X, j, x2, y2);
                }
            } else {
                double x2 = x0 + (G2 - 1);
                double y2 = y0 + G2;
                double a2 = (2.0 / 3.0) - x2 * x2 - y2 * y2;
                if(a2 > 0) {
                    value += (a2 * a2) * (a2 * a2) * gradCoord(seed, i + PRIME_X, j, x2, y2);
                }
            }

            if(yi < xmyi) {
                double x2 = x0 - G2;
                double y2 = y0 - (G2 - 1);
                double a2 = (2.0 / 3.0) - x2 * x2 - y2 * y2;
                if(a2 > 0) {
                    value += (a2 * a2) * (a2 * a2) * gradCoord(seed, i, j - PRIME_Y, x2, y2);
                }
            } else {
                double x2 = x0 + G2;
                double y2 = y0 + (G2 - 1);
                double a2 = (2.0 / 3.0) - x2 * x2 - y2 * y2;
                if(a2 > 0) {
                    value += (a2 * a2) * (a2 * a2) * gradCoord(seed, i, j + PRIME_Y, x2, y2);
                }
            }
        }

        return value * 18.24196194486065;
    }

    @Override
    @SuppressWarnings("NumericOverflow")
    public double getNoiseRaw(long sl, double x, double y, double z) {
        int seed = (int) sl;
        // 3D OpenSimplex2S case uses two offset rotated cube grids.
        final double R3 = (2.0 / 3.0);
        double r = (x + y + z) * R3; // Rotation, not skew
        x = r - x;
        y = r - y;
        z = r - z;


        int i = (int) Math.floor(x);
        int j = (int) Math.floor(y);
        int k = (int) Math.floor(z);
        double xi = x - i;
        double yi = y - j;
        double zi = z - k;

        i *= PRIME_X;
        j *= PRIME_Y;
        k *= PRIME_Z;
        int seed2 = seed + 1293373;

        int xNMask = (int) (-0.5 - xi);
        int yNMask = (int) (-0.5 - yi);
        int zNMask = (int) (-0.5 - zi);

        double x0 = xi + xNMask;
        double y0 = yi + yNMask;
        double z0 = zi + zNMask;
        double a0 = 0.75 - x0 * x0 - y0 * y0 - z0 * z0;
        double value = (a0 * a0) * (a0 * a0) * gradCoord(seed, i + (xNMask & PRIME_X), j + (yNMask & PRIME_Y), k + (zNMask & PRIME_Z), x0,
            y0,
            z0);

        double x1 = xi - 0.5;
        double y1 = yi - 0.5;
        double z1 = zi - 0.5;
        double a1 = 0.75 - x1 * x1 - y1 * y1 - z1 * z1;
        value += (a1 * a1) * (a1 * a1) * gradCoord(seed2, i + PRIME_X, j + PRIME_Y, k + PRIME_Z, x1, y1, z1);

        double xAFlipMask0 = ((xNMask | 1) << 1) * x1;
        double yAFlipMask0 = ((yNMask | 1) << 1) * y1;
        double zAFlipMask0 = ((zNMask | 1) << 1) * z1;
        double xAFlipMask1 = (-2 - (xNMask << 2)) * x1 - 1.0;
        double yAFlipMask1 = (-2 - (yNMask << 2)) * y1 - 1.0;
        double zAFlipMask1 = (-2 - (zNMask << 2)) * z1 - 1.0;

        boolean skip5 = false;
        double a2 = xAFlipMask0 + a0;
        if(a2 > 0) {
            double x2 = x0 - (xNMask | 1);
            value += (a2 * a2) * (a2 * a2) * gradCoord(seed, i + (~xNMask & PRIME_X), j + (yNMask & PRIME_Y), k + (zNMask & PRIME_Z), x2,
                y0,
                z0);
        } else {
            double a3 = yAFlipMask0 + zAFlipMask0 + a0;
            if(a3 > 0) {
                double y3 = y0 - (yNMask | 1);
                double z3 = z0 - (zNMask | 1);
                value += (a3 * a3) * (a3 * a3) * gradCoord(seed, i + (xNMask & PRIME_X), j + (~yNMask & PRIME_Y), k + (~zNMask & PRIME_Z),
                    x0,
                    y3, z3);
            }

            double a4 = xAFlipMask1 + a1;
            if(a4 > 0) {
                double x4 = (xNMask | 1) + x1;
                value += (a4 * a4) * (a4 * a4) * gradCoord(seed2, i + (xNMask & (PRIME_X << 1)), j + PRIME_Y, k + PRIME_Z, x4, y1, z1);
                skip5 = true;
            }
        }

        boolean skip9 = false;
        double a6 = yAFlipMask0 + a0;
        if(a6 > 0) {
            double y6 = y0 - (yNMask | 1);
            value += (a6 * a6) * (a6 * a6) * gradCoord(seed, i + (xNMask & PRIME_X), j + (~yNMask & PRIME_Y), k + (zNMask & PRIME_Z), x0,
                y6,
                z0);
        } else {
            double a7 = xAFlipMask0 + zAFlipMask0 + a0;
            if(a7 > 0) {
                double x7 = x0 - (xNMask | 1);
                double z7 = z0 - (zNMask | 1);
                value += (a7 * a7) * (a7 * a7) * gradCoord(seed, i + (~xNMask & PRIME_X), j + (yNMask & PRIME_Y), k + (~zNMask & PRIME_Z),
                    x7,
                    y0, z7);
            }

            double a8 = yAFlipMask1 + a1;
            if(a8 > 0) {
                double y8 = (yNMask | 1) + y1;
                value += (a8 * a8) * (a8 * a8) * gradCoord(seed2, i + PRIME_X, j + (yNMask & (PRIME_Y << 1)), k + PRIME_Z, x1, y8, z1);
                skip9 = true;
            }
        }

        boolean skipD = false;
        double aA = zAFlipMask0 + a0;
        if(aA > 0) {
            double zA = z0 - (zNMask | 1);
            value += (aA * aA) * (aA * aA) * gradCoord(seed, i + (xNMask & PRIME_X), j + (yNMask & PRIME_Y), k + (~zNMask & PRIME_Z), x0,
                y0,
                zA);
        } else {
            double aB = xAFlipMask0 + yAFlipMask0 + a0;
            if(aB > 0) {
                double xB = x0 - (xNMask | 1);
                double yB = y0 - (yNMask | 1);
                value += (aB * aB) * (aB * aB) * gradCoord(seed, i + (~xNMask & PRIME_X), j + (~yNMask & PRIME_Y), k + (zNMask & PRIME_Z),
                    xB,
                    yB, z0);
            }

            double aC = zAFlipMask1 + a1;
            if(aC > 0) {
                double zC = (zNMask | 1) + z1;
                value += (aC * aC) * (aC * aC) * gradCoord(seed2, i + PRIME_X, j + PRIME_Y, k + (zNMask & (PRIME_Z << 1)), x1, y1, zC);
                skipD = true;
            }
        }

        if(!skip5) {
            double a5 = yAFlipMask1 + zAFlipMask1 + a1;
            if(a5 > 0) {
                double y5 = (yNMask | 1) + y1;
                double z5 = (zNMask | 1) + z1;
                value += (a5 * a5) * (a5 * a5) * gradCoord(seed2, i + PRIME_X, j + (yNMask & (PRIME_Y << 1)), k + (zNMask & (PRIME_Z << 1)),
                    x1, y5, z5);
            }
        }

        if(!skip9) {
            double a9 = xAFlipMask1 + zAFlipMask1 + a1;
            if(a9 > 0) {
                double x9 = (xNMask | 1) + x1;
                double z9 = (zNMask | 1) + z1;
                value += (a9 * a9) * (a9 * a9) * gradCoord(seed2, i + (xNMask & (PRIME_X << 1)), j + PRIME_Y, k + (zNMask & (PRIME_Z << 1)),
                    x9,
                    y1, z9);
            }
        }

        if(!skipD) {
            double aD = xAFlipMask1 + yAFlipMask1 + a1;
            if(aD > 0) {
                double xD = (xNMask | 1) + x1;
                double yD = (yNMask | 1) + y1;
                value += (aD * aD) * (aD * aD) * gradCoord(seed2, i + (xNMask & (PRIME_X << 1)), j + (yNMask & (PRIME_Y << 1)), k + PRIME_Z,
                    xD, yD, z1);
            }
        }

        return value * 9.046026385208288;
    }

    @Override
    public boolean isDifferentiable() {
        return true;
    }

    @Override
    public double[] getNoiseDerivativeRaw(long sl, double x, double y) {
        int seed = (int) sl;
        // 2D OpenSimplex2S case is a modified 2D simplex noise.

        final double SQRT3 = 1.7320508075688772935274463415059;
        final double G2 = (3 - SQRT3) / 6;

        final double F2 = 0.5f * (SQRT3 - 1);
        double s = (x + y) * F2;
        x += s;
        y += s;


        int i = (int) Math.floor(x);
        int j = (int) Math.floor(y);
        double xi = x - i;
        double yi = y - j;

        i *= PRIME_X;
        j *= PRIME_Y;
        int i1 = i + PRIME_X;
        int j1 = j + PRIME_Y;

        double t = (xi + yi) * G2;
        double x0 = xi - t;
        double y0 = yi - t;

        double[] out = { 0.0f, 0.0f, 0.0f };

        double a0 = (2.0 / 3.0) - x0 * x0 - y0 * y0;
        double aa0 = a0 * a0, aaa0 = aa0 * a0, aaaa0 = aa0 * aa0;
        int gi0 = gradCoordIndex(seed, i, j);
        double gx0 = GRADIENTS_2_D[gi0], gy0 = GRADIENTS_2_D[gi0 | 1];
        double rampValue0 = gx0 * x0 + gy0 * y0;
        out[0] = aaaa0 * rampValue0;
        out[1] = gx0 * aaaa0 - 8 * rampValue0 * aaa0 * x0;
        out[2] = gy0 * aaaa0 - 8 * rampValue0 * aaa0 * y0;


        double a1 = 2 * (1 - 2 * G2) * (1 / G2 - 2) * t + ((-2 * (1 - 2 * G2) * (1 - 2 * G2)) + a0);
        double x1 = x0 - (1 - 2 * G2);
        double y1 = y0 - (1 - 2 * G2);
        double aa1 = a1 * a1, aaa1 = aa1 * a1, aaaa1 = aa1 * aa1;
        int gi1 = gradCoordIndex(seed, i1, j1);
        double gx1 = GRADIENTS_2_D[gi1], gy1 = GRADIENTS_2_D[gi1 | 1];
        double rampValue1 = gx1 * x1 + gy1 * y1;
        out[0] += aaaa1 * rampValue1;
        out[1] += gx1 * aaaa1 - 8 * rampValue1 * aaa1 * x1;
        out[2] += gy1 * aaaa1 - 8 * rampValue1 * aaa1 * y1;

        // Nested conditionals were faster than compact bit logic/arithmetic.
        double xmyi = xi - yi;
        if(t > G2) {
            if(xi + xmyi > 1) {
                double x2 = x0 + (3 * G2 - 2);
                double y2 = y0 + (3 * G2 - 1);
                double a2 = (2.0 / 3.0) - x2 * x2 - y2 * y2;
                if(a2 > 0) {
                    double aa2 = a2 * a2, aaa2 = aa2 * a2, aaaa2 = aa2 * aa2;
                    int gi2 = gradCoordIndex(seed, i + (PRIME_X << 1), j + PRIME_Y);
                    double gx2 = GRADIENTS_2_D[gi2 | 0], gy2 = GRADIENTS_2_D[gi2 | 1];
                    double rampValue2 = gx2 * x2 + gy2 * y2;
                    out[0] += aaaa2 * rampValue2;
                    out[1] += gx2 * aaaa2 - 8 * rampValue2 * aaa2 * x2;
                    out[2] += gy2 * aaaa2 - 8 * rampValue2 * aaa2 * y2;
                }
            } else {
                double x2 = x0 + G2;
                double y2 = y0 + (G2 - 1);
                double a2 = (2.0 / 3.0) - x2 * x2 - y2 * y2;
                if(a2 > 0) {
                    double aa2 = a2 * a2, aaa2 = aa2 * a2, aaaa2 = aa2 * aa2;
                    int gi2 = gradCoordIndex(seed, i, j + PRIME_Y);
                    double gx2 = GRADIENTS_2_D[gi2], gy2 = GRADIENTS_2_D[gi2 | 1];
                    double rampValue2 = gx2 * x2 + gy2 * y2;
                    out[0] += aaaa2 * rampValue2;
                    out[1] += gx2 * aaaa2 - 8 * rampValue2 * aaa2 * x2;
                    out[2] += gy2 * aaaa2 - 8 * rampValue2 * aaa2 * y2;
                }
            }

            if(yi - xmyi > 1) {
                double x3 = x0 + (3 * G2 - 1);
                double y3 = y0 + (3 * G2 - 2);
                double a3 = (2.0 / 3.0) - x3 * x3 - y3 * y3;
                if(a3 > 0) {
                    double aa3 = a3 * a3, aaa3 = aa3 * a3, aaaa3 = aa3 * aa3;
                    int gi3 = gradCoordIndex(seed, i + PRIME_X, j + (PRIME_Y << 1));
                    double gx3 = GRADIENTS_2_D[gi3], gy3 = GRADIENTS_2_D[gi3 | 1];
                    double rampValue3 = gx3 * x3 + gy3 * y3;
                    out[0] += aaaa3 * rampValue3;
                    out[1] += gx3 * aaaa3 - 8 * rampValue3 * aaa3 * x3;
                    out[2] += gy3 * aaaa3 - 8 * rampValue3 * aaa3 * y3;
                }
            } else {
                double x3 = x0 + (G2 - 1);
                double y3 = y0 + G2;
                double a3 = (2.0 / 3.0) - x3 * x3 - y3 * y3;
                if(a3 > 0) {
                    double aa3 = a3 * a3, aaa3 = aa3 * a3, aaaa3 = aa3 * aa3;
                    int gi3 = gradCoordIndex(seed, i + PRIME_X, j);
                    double gx3 = GRADIENTS_2_D[gi3], gy3 = GRADIENTS_2_D[gi3 | 1];
                    double rampValue3 = gx3 * x3 + gy3 * y3;
                    out[0] += aaaa3 * rampValue3;
                    out[1] += gx3 * aaaa3 - 8 * rampValue3 * aaa3 * x3;
                    out[2] += gy3 * aaaa3 - 8 * rampValue3 * aaa3 * y3;
                }
            }
        } else {
            if(xi + xmyi < 0) {
                double x2 = x0 + (1 - G2);
                double y2 = y0 - G2;
                double a2 = (2.0 / 3.0) - x2 * x2 - y2 * y2;
                if(a2 > 0) {
                    double aa2 = a2 * a2, aaa2 = aa2 * a2, aaaa2 = aa2 * aa2;
                    int gi2 = gradCoordIndex(seed, i - PRIME_X, j);
                    double gx2 = GRADIENTS_2_D[gi2], gy2 = GRADIENTS_2_D[gi2 | 1];
                    double rampValue2 = gx2 * x2 + gy2 * y2;
                    out[0] += aaaa2 * rampValue2;
                    out[1] += gx2 * aaaa2 - 8 * rampValue2 * aaa2 * x2;
                    out[2] += gy2 * aaaa2 - 8 * rampValue2 * aaa2 * y2;
                }
            } else {
                double x2 = x0 + (G2 - 1);
                double y2 = y0 + G2;
                double a2 = (2.0 / 3.0) - x2 * x2 - y2 * y2;
                if(a2 > 0) {
                    double aa2 = a2 * a2, aaa2 = aa2 * a2, aaaa2 = aa2 * aa2;
                    int gi2 = gradCoordIndex(seed, i + PRIME_X, j);
                    double gx2 = GRADIENTS_2_D[gi2], gy2 = GRADIENTS_2_D[gi2 | 1];
                    double rampValue2 = gx2 * x2 + gy2 * y2;
                    out[0] += aaaa2 * rampValue2;
                    out[1] += gx2 * aaaa2 - 8 * rampValue2 * aaa2 * x2;
                    out[2] += gy2 * aaaa2 - 8 * rampValue2 * aaa2 * y2;
                }
            }

            if(yi < xmyi) {
                double x2 = x0 - G2;
                double y2 = y0 - (G2 - 1);
                double a2 = (2.0 / 3.0) - x2 * x2 - y2 * y2;
                if(a2 > 0) {
                    double aa2 = a2 * a2, aaa2 = aa2 * a2, aaaa2 = aa2 * aa2;
                    int gi2 = gradCoordIndex(seed, i, j - PRIME_Y);
                    double gx2 = GRADIENTS_2_D[gi2], gy2 = GRADIENTS_2_D[gi2 | 1];
                    double rampValue2 = gx2 * x2 + gy2 * y2;
                    out[0] += aaaa2 * rampValue2;
                    out[1] += gx2 * aaaa2 - 8 * rampValue2 * aaa2 * x2;
                    out[2] += gy2 * aaaa2 - 8 * rampValue2 * aaa2 * y2;
                }
            } else {
                double x2 = x0 + G2;
                double y2 = y0 + (G2 - 1);
                double a2 = (2.0 / 3.0) - x2 * x2 - y2 * y2;
                if(a2 > 0) {
                    double aa2 = a2 * a2, aaa2 = aa2 * a2, aaaa2 = aa2 * aa2;
                    int gi2 = gradCoordIndex(seed, i, j + PRIME_Y);
                    double gx2 = GRADIENTS_2_D[gi2], gy2 = GRADIENTS_2_D[gi2 | 1];
                    double rampValue2 = gx2 * x2 + gy2 * y2;
                    out[0] += aaaa2 * rampValue2;
                    out[1] += gx2 * aaaa2 - 8 * rampValue2 * aaa2 * x2;
                    out[2] += gy2 * aaaa2 - 8 * rampValue2 * aaa2 * y2;
                }
            }
        }
        out[0] *= 18.24196194486065;
        out[1] *= 18.24196194486065;
        out[2] *= 18.24196194486065;
        return out;
    }

    @Override
    public double[] getNoiseDerivativeRaw(long sl, double x, double y, double z) {
        int seed = (int) sl;
        // 3D OpenSimplex2S case uses two offset rotated cube grids.
        final double R3 = (2.0 / 3.0);
        double r = (x + y + z) * R3; // Rotation, not skew
        x = r - x;
        y = r - y;
        z = r - z;


        int i = (int) Math.floor(x);
        int j = (int) Math.floor(y);
        int k = (int) Math.floor(z);
        double xi = x - i;
        double yi = y - j;
        double zi = z - k;

        i *= PRIME_X;
        j *= PRIME_Y;
        k *= PRIME_Z;
        int seed2 = seed + 1293373;

        int xNMask = (int) (-0.5 - xi);
        int yNMask = (int) (-0.5 - yi);
        int zNMask = (int) (-0.5 - zi);

        double[] out = { 0.0f, 0.0f, 0.0f, 0.0f };

        double x0 = xi + xNMask;
        double y0 = yi + yNMask;
        double z0 = zi + zNMask;
        double a0 = 0.75 - x0 * x0 - y0 * y0 - z0 * z0;
        double aa0 = a0 * a0, aaa0 = aa0 * a0, aaaa0 = aa0 * aa0;
        int gi0 = gradCoordIndex(seed, i + (xNMask & PRIME_X), j + (yNMask & PRIME_Y), k + (zNMask & PRIME_Z));
        double gx0 = GRADIENTS_3D[gi0], gy0 = GRADIENTS_3D[gi0 | 1], gz0 = GRADIENTS_3D[gi0 | 2];
        double rampValue0 = gx0 * x0 + gy0 * y0 + gz0 * z0;
        out[0] = aaaa0 * rampValue0;
        out[1] = gx0 * aaaa0 - 8 * rampValue0 * aaa0 * x0;
        out[2] = gy0 * aaaa0 - 8 * rampValue0 * aaa0 * y0;
        out[3] = gz0 * aaaa0 - 8 * rampValue0 * aaa0 * z0;

        double x1 = xi - 0.5;
        double y1 = yi - 0.5;
        double z1 = zi - 0.5;
        double a1 = 0.75 - x1 * x1 - y1 * y1 - z1 * z1;
        double aa1 = a1 * a1, aaa1 = aa1 * a1, aaaa1 = aa1 * aa1;
        int gi1 = gradCoordIndex(seed2, i + PRIME_X, j + PRIME_Y, k + PRIME_Z);
        double gx1 = GRADIENTS_3D[gi1], gy1 = GRADIENTS_3D[gi1 | 1], gz1 = GRADIENTS_3D[gi1 | 2];
        double rampValue1 = gx1 * x1 + gy1 * y1 + gz1 * z1;
        out[0] += aaaa1 * rampValue1;
        out[1] += gx1 * aaaa1 - 8 * rampValue1 * aaa1 * x1;
        out[2] += gy1 * aaaa1 - 8 * rampValue1 * aaa1 * y1;
        out[3] += gz1 * aaaa1 - 8 * rampValue1 * aaa1 * z1;

        double xAFlipMask0 = ((xNMask | 1) << 1) * x1;
        double yAFlipMask0 = ((yNMask | 1) << 1) * y1;
        double zAFlipMask0 = ((zNMask | 1) << 1) * z1;
        double xAFlipMask1 = (-2 - (xNMask << 2)) * x1 - 1.0;
        double yAFlipMask1 = (-2 - (yNMask << 2)) * y1 - 1.0;
        double zAFlipMask1 = (-2 - (zNMask << 2)) * z1 - 1.0;

        boolean skip5 = false;
        double a2 = xAFlipMask0 + a0;
        if(a2 > 0) {
            double x2 = x0 - (xNMask | 1);
            double aa2 = a2 * a2, aaa2 = aa2 * a2, aaaa2 = aa2 * aa2;
            int gi2 = gradCoordIndex(seed, i + (~xNMask & PRIME_X), j + (yNMask & PRIME_Y), k + (zNMask & PRIME_Z));
            double gx2 = GRADIENTS_3D[gi2], gy2 = GRADIENTS_3D[gi2 | 1], gz2 = GRADIENTS_3D[gi2 | 2];
            double rampValue2 = gx2 * x2 + gy2 * y0 + gz2 * z0;
            out[0] += aaaa2 * rampValue2;
            out[1] += gx2 * aaaa2 - 8 * rampValue2 * aaa2 * x2;
            out[2] += gy2 * aaaa2 - 8 * rampValue2 * aaa2 * y0;
            out[3] += gz2 * aaaa2 - 8 * rampValue2 * aaa2 * z0;
        } else {
            double a3 = yAFlipMask0 + zAFlipMask0 + a0;
            if(a3 > 0) {
                double y3 = y0 - (yNMask | 1);
                double z3 = z0 - (zNMask | 1);
                double aa3 = a3 * a3, aaa3 = aa3 * a3, aaaa3 = aa3 * aa3;
                int gi3 = gradCoordIndex(seed, i + (xNMask & PRIME_X), j + (~yNMask & PRIME_Y), k + (~zNMask & PRIME_Z));
                double gx3 = GRADIENTS_3D[gi3], gy3 = GRADIENTS_3D[gi3 | 1], gz3 = GRADIENTS_3D[gi3 | 2];
                double rampValue3 = gx3 * x0 + gy3 * y3 + gz3 * z3;
                out[0] += aaaa3 * rampValue3;
                out[1] += gx3 * aaaa3 - 8 * rampValue3 * aaa3 * x0;
                out[2] += gy3 * aaaa3 - 8 * rampValue3 * aaa3 * y3;
                out[3] += gz3 * aaaa3 - 8 * rampValue3 * aaa3 * z3;
            }

            double a4 = xAFlipMask1 + a1;
            if(a4 > 0) {
                double x4 = (xNMask | 1) + x1;
                double aa4 = a4 * a4, aaa4 = aa4 * a4, aaaa4 = aa4 * aa4;
                int gi4 = gradCoordIndex(seed2, i + (xNMask & (PRIME_X << 1)), j + PRIME_Y, k + PRIME_Z);
                double gx4 = GRADIENTS_3D[gi4], gy4 = GRADIENTS_3D[gi4 | 1], gz4 = GRADIENTS_3D[gi4 | 2];
                double rampValue4 = gx4 * x4 + gy4 * y1 + gz4 * z1;
                out[0] += aaaa4 * rampValue4;
                out[1] += gx4 * aaaa4 - 8 * rampValue4 * aaa4 * x4;
                out[2] += gy4 * aaaa4 - 8 * rampValue4 * aaa4 * y1;
                out[3] += gz4 * aaaa4 - 8 * rampValue4 * aaa4 * z1;
                skip5 = true;
            }
        }

        boolean skip9 = false;
        double a6 = yAFlipMask0 + a0;
        if(a6 > 0) {
            double y6 = y0 - (yNMask | 1);
            double aa6 = a6 * a6, aaa6 = aa6 * a6, aaaa6 = aa6 * aa6;
            int gi6 = gradCoordIndex(seed, i + (xNMask & PRIME_X), j + (~yNMask & PRIME_Y), k + (zNMask & PRIME_Z));
            double gx6 = GRADIENTS_3D[gi6], gy6 = GRADIENTS_3D[gi6 | 1], gz6 = GRADIENTS_3D[gi6 | 2];
            double rampValue6 = gx6 * x0 + gy6 * y6 + gz6 * z0;
            out[0] += aaaa6 * rampValue6;
            out[1] += gx6 * aaaa6 - 8 * rampValue6 * aaa6 * x0;
            out[2] += gy6 * aaaa6 - 8 * rampValue6 * aaa6 * y6;
            out[3] += gz6 * aaaa6 - 8 * rampValue6 * aaa6 * z0;
        } else {
            double a7 = xAFlipMask0 + zAFlipMask0 + a0;
            if(a7 > 0) {
                double x7 = x0 - (xNMask | 1);
                double z7 = z0 - (zNMask | 1);
                double aa7 = a7 * a7, aaa7 = aa7 * a7, aaaa7 = aa7 * aa7;
                int gi7 = gradCoordIndex(seed, i + (~xNMask & PRIME_X), j + (yNMask & PRIME_Y), k + (~zNMask & PRIME_Z));
                double gx7 = GRADIENTS_3D[gi7], gy7 = GRADIENTS_3D[gi7 | 1], gz7 = GRADIENTS_3D[gi7 | 2];
                double rampValue7 = gx7 * x7 + gy7 * y0 + gz7 * z7;
                out[0] += aaaa7 * rampValue7;
                out[1] += gx7 * aaaa7 - 8 * rampValue7 * aaa7 * x7;
                out[2] += gy7 * aaaa7 - 8 * rampValue7 * aaa7 * y0;
                out[3] += gz7 * aaaa7 - 8 * rampValue7 * aaa7 * z7;
            }

            double a8 = yAFlipMask1 + a1;
            if(a8 > 0) {
                double y8 = (yNMask | 1) + y1;
                double aa8 = a8 * a8, aaa8 = aa8 * a8, aaaa8 = aa8 * aa8;
                int gi8 = gradCoordIndex(seed2, i + PRIME_X, j + (yNMask & (PRIME_Y << 1)), k + PRIME_Z);
                double gx8 = GRADIENTS_3D[gi8], gy8 = GRADIENTS_3D[gi8 | 1], gz8 = GRADIENTS_3D[gi8 | 2];
                double rampValue8 = gx8 * x1 + gy8 * y8 + gz8 * z1;
                out[0] += aaaa8 * rampValue8;
                out[1] += gx8 * aaaa8 - 8 * rampValue8 * aaa8 * x1;
                out[2] += gy8 * aaaa8 - 8 * rampValue8 * aaa8 * y8;
                out[3] += gz8 * aaaa8 - 8 * rampValue8 * aaa8 * z1;
                skip9 = true;
            }
        }

        boolean skipD = false;
        double aA = zAFlipMask0 + a0;
        if(aA > 0) {
            double zA = z0 - (zNMask | 1);
            double aaA = aA * aA, aaaA = aaA * aA, aaaaA = aaA * aaA;
            int giA = gradCoordIndex(seed, i + (xNMask & PRIME_X), j + (yNMask & PRIME_Y), k + (~zNMask & PRIME_Z));
            double gxA = GRADIENTS_3D[giA], gyA = GRADIENTS_3D[giA | 1], gzA = GRADIENTS_3D[giA | 2];
            double rampValueA = gxA * x0 + gyA * y0 + gzA * zA;
            out[0] += aaaaA * rampValueA;
            out[1] += gxA * aaaaA - 8 * rampValueA * aaaA * x0;
            out[2] += gyA * aaaaA - 8 * rampValueA * aaaA * y0;
            out[3] += gzA * aaaaA - 8 * rampValueA * aaaA * zA;
        } else {
            double aB = xAFlipMask0 + yAFlipMask0 + a0;
            if(aB > 0) {
                double xB = x0 - (xNMask | 1);
                double yB = y0 - (yNMask | 1);
                double aaB = aB * aB, aaaB = aaB * aB, aaaaB = aaB * aaB;
                int giB = gradCoordIndex(seed, i + (~xNMask & PRIME_X), j + (~yNMask & PRIME_Y), k + (zNMask & PRIME_Z));
                double gxB = GRADIENTS_3D[giB], gyB = GRADIENTS_3D[giB | 1], gzB = GRADIENTS_3D[giB | 2];
                double rampValueB = gxB * xB + gyB * yB + gzB * z0;
                out[0] += aaaaB * rampValueB;
                out[1] += gxB * aaaaB - 8 * rampValueB * aaaB * xB;
                out[2] += gyB * aaaaB - 8 * rampValueB * aaaB * yB;
                out[3] += gzB * aaaaB - 8 * rampValueB * aaaB * z0;
            }

            double aC = zAFlipMask1 + a1;
            if(aC > 0) {
                double zC = (zNMask | 1) + z1;
                double aaC = aC * aC, aaaC = aaC * aC, aaaaC = aaC * aaC;
                int giC = gradCoordIndex(seed2, i + PRIME_X, j + PRIME_Y, k + (zNMask & (PRIME_Z << 1)));
                double gxC = GRADIENTS_3D[giC], gyC = GRADIENTS_3D[giC | 1], gzC = GRADIENTS_3D[giC | 2];
                double rampValueC = gxC * x1 + gyC * y1 + gzC * zC;
                out[0] += aaaaC * rampValueC;
                out[1] += gxC * aaaaC - 8 * rampValueC * aaaC * x1;
                out[2] += gyC * aaaaC - 8 * rampValueC * aaaC * y1;
                out[3] += gzC * aaaaC - 8 * rampValueC * aaaC * zC;
                skipD = true;
            }
        }

        if(!skip5) {
            double a5 = yAFlipMask1 + zAFlipMask1 + a1;
            if(a5 > 0) {
                double y5 = (yNMask | 1) + y1;
                double z5 = (zNMask | 1) + z1;
                double aa5 = a5 * a5, aaa5 = aa5 * a5, aaaa5 = aa5 * aa5;
                int gi5 = gradCoordIndex(seed2, i + PRIME_X, j + (yNMask & (PRIME_Y << 1)), k + (zNMask & (PRIME_Z << 1)));
                double gx5 = GRADIENTS_3D[gi5], gy5 = GRADIENTS_3D[gi5 | 1], gz5 = GRADIENTS_3D[gi5 | 2];
                double rampValue5 = gx5 * x1 + gy5 * y5 + gz5 * z5;
                out[0] += aaaa5 * rampValue5;
                out[1] += gx5 * aaaa5 - 8 * rampValue5 * aaa5 * x1;
                out[2] += gy5 * aaaa5 - 8 * rampValue5 * aaa5 * y5;
                out[3] += gz5 * aaaa5 - 8 * rampValue5 * aaa5 * z5;
            }
        }

        if(!skip9) {
            double a9 = xAFlipMask1 + zAFlipMask1 + a1;
            if(a9 > 0) {
                double x9 = (xNMask | 1) + x1;
                double z9 = (zNMask | 1) + z1;
                double aa9 = a9 * a9, aaa9 = aa9 * a9, aaaa9 = aa9 * aa9;
                int gi9 = gradCoordIndex(seed2, i + (xNMask & (PRIME_X << 1)), j + PRIME_Y, k + (zNMask & (PRIME_Z << 1)));
                double gx9 = GRADIENTS_3D[gi9], gy9 = GRADIENTS_3D[gi9 | 1], gz9 = GRADIENTS_3D[gi9 | 2];
                double rampValue9 = gx9 * x9 + gy9 * y1 + gz9 * z9;
                out[0] += aaaa9 * rampValue9;
                out[1] += gx9 * aaaa9 - 8 * rampValue9 * aaa9 * x9;
                out[2] += gy9 * aaaa9 - 8 * rampValue9 * aaa9 * y1;
                out[3] += gz9 * aaaa9 - 8 * rampValue9 * aaa9 * z9;
            }
        }

        if(!skipD) {
            double aD = xAFlipMask1 + yAFlipMask1 + a1;
            if(aD > 0) {
                double xD = (xNMask | 1) + x1;
                double yD = (yNMask | 1) + y1;
                double aaD = aD * aD, aaaD = aaD * aD, aaaaD = aaD * aaD;
                int giD = gradCoordIndex(seed2, i + (xNMask & (PRIME_X << 1)), j + (yNMask & (PRIME_Y << 1)), k + PRIME_Z);
                double gxD = GRADIENTS_3D[giD], gyD = GRADIENTS_3D[giD | 1], gzD = GRADIENTS_3D[giD | 2];
                double rampValueD = gxD * xD + gyD * yD + gzD * z1;
                out[0] += aaaaD * rampValueD;
                out[1] += gxD * aaaaD - 8 * rampValueD * aaaD * xD;
                out[2] += gyD * aaaaD - 8 * rampValueD * aaaD * yD;
                out[3] += gzD * aaaaD - 8 * rampValueD * aaaD * z1;
            }
        }

        out[0] *= 9.046026385208288;
        out[1] *= 9.046026385208288;
        out[2] *= 9.046026385208288;
        out[3] *= 9.046026385208288;
        return out;
    }
}
