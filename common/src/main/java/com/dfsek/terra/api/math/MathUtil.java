package com.dfsek.terra.api.math;

import com.dfsek.terra.api.util.FastRandom;
import com.dfsek.terra.generation.math.Sampler;
import net.jafama.FastMath;

import java.util.List;
import java.util.Random;

/**
 * Utility class for mathematical functions.
 */
public final class MathUtil {
    /**
     * Epsilon for fuzzy floating point comparisons.
     */
    public static final double EPSILON = 1.0E-5;
    /**
     * Derivative constant.
     */
    private static final double DERIVATIVE_DIST = 0.55;

    /**
     * Gets the standard deviation of an array of doubles.
     *
     * @param numArray The array of numbers to calculate the standard deviation of.
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

    /**
     * Gets the carver seed for a chunk.
     *
     * @param chunkX Chunk's X coordinate
     * @param chunkZ Chunk's Z coordinate
     * @param seed   World seed
     * @return long - The carver seed.
     */
    public static long getCarverChunkSeed(int chunkX, int chunkZ, long seed) {
        Random r = new FastRandom(seed);
        return chunkX * r.nextLong() ^ chunkZ * r.nextLong() ^ seed;
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
     * @return Whether these values are equal
     */
    public static boolean equals(double a, double b) {
        return a == b || FastMath.abs(a - b) < EPSILON;
    }

    public static double derivative(Sampler sampler, double x, double y, double z) {
        double baseSample = sampler.sample(x, y, z);

        double xVal1 = (sampler.sample(x + DERIVATIVE_DIST, y, z) - baseSample) / DERIVATIVE_DIST;
        double xVal2 = (sampler.sample(x - DERIVATIVE_DIST, y, z) - baseSample) / DERIVATIVE_DIST;
        double zVal1 = (sampler.sample(x, y, z + DERIVATIVE_DIST) - baseSample) / DERIVATIVE_DIST;
        double zVal2 = (sampler.sample(x, y, z - DERIVATIVE_DIST) - baseSample) / DERIVATIVE_DIST;
        double yVal1 = (sampler.sample(x, y + DERIVATIVE_DIST, z) - baseSample) / DERIVATIVE_DIST;
        double yVal2 = (sampler.sample(x, y - DERIVATIVE_DIST, z) - baseSample) / DERIVATIVE_DIST;

        return Math.sqrt(((xVal2 - xVal1) * (xVal2 - xVal1)) + ((zVal2 - zVal1) * (zVal2 - zVal1)) + ((yVal2 - yVal1) * (yVal2 - yVal1)));
    }

    public static int normalizeIndex(double val, int size) {
        return FastMath.max(FastMath.min(FastMath.floorToInt(((val + 1D) / 2D) * size), size - 1), 0);
    }

    public static long squash(int first, int last) {
        return (((long) first) << 32) | (last & 0xffffffffL);
    }
}
