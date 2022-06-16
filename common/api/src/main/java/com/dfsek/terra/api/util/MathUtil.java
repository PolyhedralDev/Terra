/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.util;

import net.jafama.FastMath;

import java.util.List;


/**
 * Utility class for mathematical functions.
 */
public final class MathUtil {
    /**
     * Epsilon for fuzzy floating point comparisons.
     */
    public static final double EPSILON = 1.0E-5;
    
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
            standardDeviation += FastMath.pow2(num.doubleValue() - mean);
        }
        
        return FastMath.sqrt(standardDeviation / length);
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
        return a == b || FastMath.abs(a - b) < EPSILON;
    }
    
    public static int normalizeIndex(double val, int size) {
        return FastMath.max(FastMath.min(FastMath.floorToInt(((val + 1D) / 2D) * size), size - 1), 0);
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
        return FastMath.min(FastMath.max(in, -1), 1);
    }
    
    public static int clamp(int min, int i, int max) {
        return FastMath.max(FastMath.min(i, max), min);
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
        
        if(FastMath.abs(q) <= .425) {
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
            
            r = FastMath.sqrt(-FastMath.log(r));
            
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
}
