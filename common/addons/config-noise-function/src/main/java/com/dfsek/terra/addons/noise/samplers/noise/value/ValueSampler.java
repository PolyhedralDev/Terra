/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.samplers.noise.value;

public class ValueSampler extends ValueStyleNoise {
    @Override
    public double getNoiseRaw(long sl, double x, double y) {
        int seed = (int) sl;
        int x0 = fastFloor(x);
        int y0 = fastFloor(y);
        
        double xs = interpHermite(x - x0);
        double ys = interpHermite(y - y0);
        
        x0 *= PRIME_X;
        y0 *= PRIME_Y;
        int x1 = x0 + PRIME_X;
        int y1 = y0 + PRIME_Y;
        
        double xf0 = lerp(valCoord(seed, x0, y0), valCoord(seed, x1, y0), xs);
        double xf1 = lerp(valCoord(seed, x0, y1), valCoord(seed, x1, y1), xs);
        
        return lerp(xf0, xf1, ys);
    }
    
    @Override
    public double getNoiseRaw(long sl, double x, double y, double z) {
        int seed = (int) sl;
        int x0 = fastFloor(x);
        int y0 = fastFloor(y);
        int z0 = fastFloor(z);
        
        double xs = interpHermite(x - x0);
        double ys = interpHermite(y - y0);
        double zs = interpHermite(z - z0);
        
        x0 *= PRIME_X;
        y0 *= PRIME_Y;
        z0 *= PRIME_Z;
        int x1 = x0 + PRIME_X;
        int y1 = y0 + PRIME_Y;
        int z1 = z0 + PRIME_Z;
        
        double xf00 = lerp(valCoord(seed, x0, y0, z0), valCoord(seed, x1, y0, z0), xs);
        double xf10 = lerp(valCoord(seed, x0, y1, z0), valCoord(seed, x1, y1, z0), xs);
        double xf01 = lerp(valCoord(seed, x0, y0, z1), valCoord(seed, x1, y0, z1), xs);
        double xf11 = lerp(valCoord(seed, x0, y1, z1), valCoord(seed, x1, y1, z1), xs);
        
        double yf0 = lerp(xf00, xf10, ys);
        double yf1 = lerp(xf01, xf11, ys);
        
        return lerp(yf0, yf1, zs);
    }
}
