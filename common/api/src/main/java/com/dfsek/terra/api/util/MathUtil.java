/*
 * Copyright (c) 2020-2023 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.util;

import java.util.List;


/**
 * Utility class for mathematical functions.
 */
public final class MathUtil {
    /**
     * Epsilon for fuzzy floating point comparisons.
     */
    public static final double EPSILON = 1.0E-5;
    private static final int SIN_BITS, SIN_MASK, SIN_COUNT;
    private static final double radFull, radToIndex;
    private static final double degFull, degToIndex;
    private static final double[] sin, cos;
    static {
        SIN_BITS = 12;
        SIN_MASK = ~(-1 << SIN_BITS);
        SIN_COUNT = SIN_MASK + 1;

        radFull = Math.PI * 2.0;
        degFull = 360.0;
        radToIndex = SIN_COUNT / radFull;
        degToIndex = SIN_COUNT / degFull;

        sin = new double[SIN_COUNT];
        cos = new double[SIN_COUNT];

        for(int i = 0; i < SIN_COUNT; i++) {
            sin[i] = Math.sin((i + 0.5f) / SIN_COUNT * radFull);
            cos[i] = Math.cos((i + 0.5f) / SIN_COUNT * radFull);
        }

        // Four cardinal directions (credits: Nate)
        for(int i = 0; i < 360; i += 90) {
            sin[(int) (i * degToIndex) & SIN_MASK] = Math.sin(i * Math.PI / 180.0);
            cos[(int) (i * degToIndex) & SIN_MASK] = Math.cos(i * Math.PI / 180.0);
        }
    }

    public static double sin(double rad) {
        return sin[(int) (rad * radToIndex) & SIN_MASK];
    }

    public static double cos(double rad) {
        return cos[(int) (rad * radToIndex) & SIN_MASK];
    }

    public static double tan(double rad) {
        return sin(rad) / cos(rad);
    }

    public static double invSqrt(double x) {
        double halfX = 0.5d * x;
        long i = Double.doubleToLongBits(x); // evil floating point bit level hacking
        i = 0x5FE6EC85E7DE30DAL - (i >> 1); // what the fuck?
        double y = Double.longBitsToDouble(i);
        y *= (1.5d - halfX * y * y); // 1st newtonian iteration
        // y *= (1.5d - halfX * y * y); // 2nd newtonian iteration, this can be removed

        return y;
    }

    /**
     * Gets the standard deviation of an array of doubles.
     *
     * @param numArray The array of numbers to calculate the standard deviation of.
     *
     * @return double - The standard deviation.
     */
    public static double standardDeviation(List<Number> numArray) {
        double sum = 0.0, standardDeviation = 0.0;
        int length = numArray.size();

        for(Number num : numArray) {
            sum += num.doubleValue();
        }

        double mean = sum / length;

        for(Number num : numArray) {
            standardDeviation += Math.pow(num.doubleValue() - mean, 2);
        }

        return Math.sqrt(standardDeviation / length);
    }

    public static long hashToLong(String s) {
        if(s == null) {
            return 0;
        }
        long hash = 0;
        for(char c : s.toCharArray()) {
            hash = 31L * hash + c;
        }
        return hash;
    }

    /**
     * Compare 2 floating-point values with epsilon to account for rounding errors
     *
     * @param a Value 1
     * @param b Value 2
     *
     * @return Whether these values are equal
     */
    public static boolean equals(double a, double b) {
        return a == b || Math.abs(a - b) < EPSILON;
    }

    public static int normalizeIndex(double val, int size) {
        return Math.max(Math.min((int) Math.floor(((val + 1D) / 2D) * size), size - 1), 0);
    }

    public static long squash(int first, int last) {
        return (((long) first) << 32) | (last & 0xffffffffL);
    }

    /**
     * Clamp value to range of [-1, 1]
     *
     * @param in Value to clamp
     *
     * @return Clamped value
     */
    public static double clamp(double in) {
        return Math.min(Math.max(in, -1), 1);
    }

    public static int clamp(int min, int i, int max) {
        return Math.max(Math.min(i, max), min);
    }

    /**
     * Compute the value in a normally distributed data set that has probability p.
     *
     * @param p     Probability of value
     * @param mu    Mean of data
     * @param sigma Standard deviation of data
     *
     * @return Value corresponding to input probability
     */
    public static double normalInverse(double p, double mu, double sigma) {
        if(p < 0 || p > 1)
            throw new IllegalArgumentException("Probability must be in range [0, 1]");
        if(sigma < 0)
            throw new IllegalArgumentException("Standard deviation must be positive.");
        if(p == 0)
            return Double.NEGATIVE_INFINITY;
        if(p == 1)
            return Double.POSITIVE_INFINITY;
        if(sigma == 0)
            return mu;
        double q, r, val;

        q = p - 0.5;

        if(Math.abs(q) <= .425) {
            r = .180625 - q * q;
            val =
                q * (((((((r * 2509.0809287301226727 +
                           33430.575583588128105) * r + 67265.770927008700853) * r +
                         45921.953931549871457) * r + 13731.693765509461125) * r +
                       1971.5909503065514427) * r + 133.14166789178437745) * r +
                     3.387132872796366608)
                / (((((((r * 5226.495278852854561 +
                         28729.085735721942674) * r + 39307.89580009271061) * r +
                       21213.794301586595867) * r + 5394.1960214247511077) * r +
                     687.1870074920579083) * r + 42.313330701600911252) * r + 1);
        } else {
            if(q > 0) {
                r = 1 - p;
            } else {
                r = p;
            }

            r = Math.sqrt(-Math.log(r));

            if(r <= 5) {
                r -= 1.6;
                val = (((((((r * 7.7454501427834140764e-4 +
                             .0227238449892691845833) * r + .24178072517745061177) *
                           r + 1.27045825245236838258) * r +
                          3.64784832476320460504) * r + 5.7694972214606914055) *
                        r + 4.6303378461565452959) * r +
                       1.42343711074968357734)
                      / (((((((r *
                               1.05075007164441684324e-9 + 5.475938084995344946e-4) *
                              r + .0151986665636164571966) * r +
                             .14810397642748007459) * r + .68976733498510000455) *
                           r + 1.6763848301838038494) * r +
                          2.05319162663775882187) * r + 1);
            } else {
                r -= 5;
                val = (((((((r * 2.01033439929228813265e-7 +
                             2.71155556874348757815e-5) * r +
                            .0012426609473880784386) * r + .026532189526576123093) *
                          r + .29656057182850489123) * r +
                         1.7848265399172913358) * r + 5.4637849111641143699) *
                       r + 6.6579046435011037772)
                      / (((((((r *
                               2.04426310338993978564e-15 + 1.4215117583164458887e-7) *
                              r + 1.8463183175100546818e-5) * r +
                             7.868691311456132591e-4) * r + .0148753612908506148525)
                           * r + .13692988092273580531) * r +
                          .59983220655588793769) * r + 1);
            }

            if(q < 0.0) {
                val = -val;
            }
        }

        return mu + sigma * val;
    }

    /**
     * Murmur64 hashing function
     *
     * @param h Input value
     *
     * @return Hashed value
     */
    public static long murmur64(long h) {
        h ^= h >>> 33;
        h *= 0xff51afd7ed558ccdL;
        h ^= h >>> 33;
        h *= 0xc4ceb9fe1a85ec53L;
        h ^= h >>> 33;
        return h;
    }

    /**
     * 1D Linear interpolation between 2 points 1 unit apart.
     *
     * @param t  - Distance from v0. Total distance between v0 and v1 is 1 unit.
     * @param v0 - Value at v0.
     * @param v1 - Value at v1.
     *
     * @return double - The interpolated value.
     */
    public static double lerp(double t, double v0, double v1) {
        return v0 + t * (v1 - v0);
    }

    public static double cubicLerp(double a, double b, double c, double d, double t) {
        double p = (d - c) - (a - b);
        return t * t * t * p + t * t * ((a - b) - p) + t * (c - a) + b;
    }

    public static double interpHermite(double t) {
        return t * t * (3 - 2 * t);
    }

    public static double interpQuintic(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }
}
