/*
 * Copyright (c) 2020-2023 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.samplers.noise.value;

import com.dfsek.terra.api.util.MathUtil;


public class ValueSampler extends ValueStyleNoise {
    @Override
    public double getNoiseRaw(long sl, double x, double y) {
        int seed = (int) sl;
        int x0 = (int) Math.floor(x);
        int y0 = (int) Math.floor(y);

        double xs = MathUtil.interpHermite(x - x0);
        double ys = MathUtil.interpHermite(y - y0);

        x0 *= PRIME_X;
        y0 *= PRIME_Y;
        int x1 = x0 + PRIME_X;
        int y1 = y0 + PRIME_Y;

        double xf0 = MathUtil.lerp(xs, valCoord(seed, x0, y0), valCoord(seed, x1, y0));
        double xf1 = MathUtil.lerp(xs, valCoord(seed, x0, y1), valCoord(seed, x1, y1));

        return MathUtil.lerp(ys, xf0, xf1);
    }

    @Override
    public double getNoiseRaw(long sl, double x, double y, double z) {
        int seed = (int) sl;
        int x0 = (int) Math.floor(x);
        int y0 = (int) Math.floor(y);
        int z0 = (int) Math.floor(z);

        double xs = MathUtil.interpHermite(x - x0);
        double ys = MathUtil.interpHermite(y - y0);
        double zs = MathUtil.interpHermite(z - z0);

        x0 *= PRIME_X;
        y0 *= PRIME_Y;
        z0 *= PRIME_Z;
        int x1 = x0 + PRIME_X;
        int y1 = y0 + PRIME_Y;
        int z1 = z0 + PRIME_Z;

        double xf00 = MathUtil.lerp(xs, valCoord(seed, x0, y0, z0), valCoord(seed, x1, y0, z0));
        double xf10 = MathUtil.lerp(xs, valCoord(seed, x0, y1, z0), valCoord(seed, x1, y1, z0));
        double xf01 = MathUtil.lerp(xs, valCoord(seed, x0, y0, z1), valCoord(seed, x1, y0, z1));
        double xf11 = MathUtil.lerp(xs, valCoord(seed, x0, y1, z1), valCoord(seed, x1, y1, z1));

        double yf0 = MathUtil.lerp(ys, xf00, xf10);
        double yf1 = MathUtil.lerp(ys, xf01, xf11);

        return MathUtil.lerp(zs, yf0, yf1);
    }
}
