/*
 * Copyright (c) 2020-2021 Polyhedral Development
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
        
        
        int i = fastFloor(x);
        int j = fastFloor(y);
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
        
        
        int i = fastFloor(x);
        int j = fastFloor(y);
        int k = fastFloor(z);
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
}
