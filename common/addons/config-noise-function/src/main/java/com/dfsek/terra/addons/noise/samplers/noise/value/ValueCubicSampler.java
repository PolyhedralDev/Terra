/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.samplers.noise.value;

public class ValueCubicSampler extends ValueStyleNoise {
    @Override
    public double getNoiseRaw(long sl, double x, double y) {
        int seed = (int) sl;
        int x1 = fastFloor(x);
        int y1 = fastFloor(y);
        
        double xs = x - x1;
        double ys = y - y1;
        
        x1 *= PRIME_X;
        y1 *= PRIME_Y;
        int x0 = x1 - PRIME_X;
        int y0 = y1 - PRIME_Y;
        int x2 = x1 + PRIME_X;
        int y2 = y1 + PRIME_Y;
        int x3 = x1 + (PRIME_X << 1);
        int y3 = y1 + (PRIME_Y << 1);
        
        return cubicLerp(
                cubicLerp(valCoord(seed, x0, y0), valCoord(seed, x1, y0), valCoord(seed, x2, y0), valCoord(seed, x3, y0),
                          xs),
                cubicLerp(valCoord(seed, x0, y1), valCoord(seed, x1, y1), valCoord(seed, x2, y1), valCoord(seed, x3, y1),
                          xs),
                cubicLerp(valCoord(seed, x0, y2), valCoord(seed, x1, y2), valCoord(seed, x2, y2), valCoord(seed, x3, y2),
                          xs),
                cubicLerp(valCoord(seed, x0, y3), valCoord(seed, x1, y3), valCoord(seed, x2, y3), valCoord(seed, x3, y3),
                          xs),
                ys) * (1 / (1.5 * 1.5));
    }
    
    @Override
    public double getNoiseRaw(long sl, double x, double y, double z) {
        int seed = (int) sl;
        int x1 = fastFloor(x);
        int y1 = fastFloor(y);
        int z1 = fastFloor(z);
        
        double xs = x - x1;
        double ys = y - y1;
        double zs = z - z1;
        
        x1 *= PRIME_X;
        y1 *= PRIME_Y;
        z1 *= PRIME_Z;
        
        int x0 = x1 - PRIME_X;
        int y0 = y1 - PRIME_Y;
        int z0 = z1 - PRIME_Z;
        int x2 = x1 + PRIME_X;
        int y2 = y1 + PRIME_Y;
        int z2 = z1 + PRIME_Z;
        int x3 = x1 + (PRIME_X << 1);
        int y3 = y1 + (PRIME_Y << 1);
        int z3 = z1 + (PRIME_Z << 1);
        
        return cubicLerp(
                cubicLerp(
                        cubicLerp(valCoord(seed, x0, y0, z0), valCoord(seed, x1, y0, z0), valCoord(seed, x2, y0, z0),
                                  valCoord(seed, x3, y0, z0), xs),
                        cubicLerp(valCoord(seed, x0, y1, z0), valCoord(seed, x1, y1, z0), valCoord(seed, x2, y1, z0),
                                  valCoord(seed, x3, y1, z0), xs),
                        cubicLerp(valCoord(seed, x0, y2, z0), valCoord(seed, x1, y2, z0), valCoord(seed, x2, y2, z0),
                                  valCoord(seed, x3, y2, z0), xs),
                        cubicLerp(valCoord(seed, x0, y3, z0), valCoord(seed, x1, y3, z0), valCoord(seed, x2, y3, z0),
                                  valCoord(seed, x3, y3, z0), xs),
                        ys),
                cubicLerp(
                        cubicLerp(valCoord(seed, x0, y0, z1), valCoord(seed, x1, y0, z1), valCoord(seed, x2, y0, z1),
                                  valCoord(seed, x3, y0, z1), xs),
                        cubicLerp(valCoord(seed, x0, y1, z1), valCoord(seed, x1, y1, z1), valCoord(seed, x2, y1, z1),
                                  valCoord(seed, x3, y1, z1), xs),
                        cubicLerp(valCoord(seed, x0, y2, z1), valCoord(seed, x1, y2, z1), valCoord(seed, x2, y2, z1),
                                  valCoord(seed, x3, y2, z1), xs),
                        cubicLerp(valCoord(seed, x0, y3, z1), valCoord(seed, x1, y3, z1), valCoord(seed, x2, y3, z1),
                                  valCoord(seed, x3, y3, z1), xs),
                        ys),
                cubicLerp(
                        cubicLerp(valCoord(seed, x0, y0, z2), valCoord(seed, x1, y0, z2), valCoord(seed, x2, y0, z2),
                                  valCoord(seed, x3, y0, z2), xs),
                        cubicLerp(valCoord(seed, x0, y1, z2), valCoord(seed, x1, y1, z2), valCoord(seed, x2, y1, z2),
                                  valCoord(seed, x3, y1, z2), xs),
                        cubicLerp(valCoord(seed, x0, y2, z2), valCoord(seed, x1, y2, z2), valCoord(seed, x2, y2, z2),
                                  valCoord(seed, x3, y2, z2), xs),
                        cubicLerp(valCoord(seed, x0, y3, z2), valCoord(seed, x1, y3, z2), valCoord(seed, x2, y3, z2),
                                  valCoord(seed, x3, y3, z2), xs),
                        ys),
                cubicLerp(
                        cubicLerp(valCoord(seed, x0, y0, z3), valCoord(seed, x1, y0, z3), valCoord(seed, x2, y0, z3),
                                  valCoord(seed, x3, y0, z3), xs),
                        cubicLerp(valCoord(seed, x0, y1, z3), valCoord(seed, x1, y1, z3), valCoord(seed, x2, y1, z3),
                                  valCoord(seed, x3, y1, z3), xs),
                        cubicLerp(valCoord(seed, x0, y2, z3), valCoord(seed, x1, y2, z3), valCoord(seed, x2, y2, z3),
                                  valCoord(seed, x3, y2, z3), xs),
                        cubicLerp(valCoord(seed, x0, y3, z3), valCoord(seed, x1, y3, z3), valCoord(seed, x2, y3, z3),
                                  valCoord(seed, x3, y3, z3), xs),
                        ys),
                zs) * (1 / (1.5 * 1.5 * 1.5));
    }
}
