/*
 * Copyright (c) 2020-2023 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.samplers.noise.simplex;

import com.dfsek.terra.api.util.MathUtil;


/**
 * NoiseSampler implementation to provide Perlin Noise.
 */
public class PerlinSampler extends SimplexStyleSampler {
    @Override
    public double getNoiseRaw(long sl, double x, double y) {
        int seed = (int) sl;
        int x0 = (int) Math.floor(x);
        int y0 = (int) Math.floor(y);

        double xd0 = x - x0;
        double yd0 = y - y0;
        double xd1 = xd0 - 1;
        double yd1 = yd0 - 1;

        double xs = MathUtil.interpQuintic(xd0);
        double ys = MathUtil.interpQuintic(yd0);

        x0 *= PRIME_X;
        y0 *= PRIME_Y;
        int x1 = x0 + PRIME_X;
        int y1 = y0 + PRIME_Y;

        double xf0 = MathUtil.lerp(gradCoord(seed, x0, y0, xd0, yd0), gradCoord(seed, x1, y0, xd1, yd0), xs);
        double xf1 = MathUtil.lerp(gradCoord(seed, x0, y1, xd0, yd1), gradCoord(seed, x1, y1, xd1, yd1), xs);

        return MathUtil.lerp(xf0, xf1, ys) * 1.4247691104677813;
    }

    @Override
    public double getNoiseRaw(long sl, double x, double y, double z) {
        int seed = (int) sl;
        int x0 = (int) Math.floor(x);
        int y0 = (int) Math.floor(y);
        int z0 = (int) Math.floor(z);

        double xd0 = x - x0;
        double yd0 = y - y0;
        double zd0 = z - z0;
        double xd1 = xd0 - 1;
        double yd1 = yd0 - 1;
        double zd1 = zd0 - 1;

        double xs = MathUtil.interpQuintic(xd0);
        double ys = MathUtil.interpQuintic(yd0);
        double zs = MathUtil.interpQuintic(zd0);

        x0 *= PRIME_X;
        y0 *= PRIME_Y;
        z0 *= PRIME_Z;
        int x1 = x0 + PRIME_X;
        int y1 = y0 + PRIME_Y;
        int z1 = z0 + PRIME_Z;

        double xf00 = MathUtil.lerp(gradCoord(seed, x0, y0, z0, xd0, yd0, zd0), gradCoord(seed, x1, y0, z0, xd1, yd0, zd0), xs);
        double xf10 = MathUtil.lerp(gradCoord(seed, x0, y1, z0, xd0, yd1, zd0), gradCoord(seed, x1, y1, z0, xd1, yd1, zd0), xs);
        double xf01 = MathUtil.lerp(gradCoord(seed, x0, y0, z1, xd0, yd0, zd1), gradCoord(seed, x1, y0, z1, xd1, yd0, zd1), xs);
        double xf11 = MathUtil.lerp(gradCoord(seed, x0, y1, z1, xd0, yd1, zd1), gradCoord(seed, x1, y1, z1, xd1, yd1, zd1), xs);

        double yf0 = MathUtil.lerp(xf00, xf10, ys);
        double yf1 = MathUtil.lerp(xf01, xf11, ys);

        return MathUtil.lerp(yf0, yf1, zs) * 0.964921414852142333984375;
    }
}
