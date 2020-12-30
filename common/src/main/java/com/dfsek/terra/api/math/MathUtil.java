package com.dfsek.terra.api.math;

import com.dfsek.terra.api.util.FastRandom;
import net.jafama.FastMath;

import java.util.Random;

/**
 * Utility class for mathematical functions.
 */
public class MathUtil {
    /**
     * Gets the standard deviation of an array of doubles.
     *
     * @param numArray The array of numbers to calculate the standard deviation of.
     * @return double - The standard deviation.
     */
    public static double standardDeviation(double[] numArray) {
        double sum = 0.0, standardDeviation = 0.0;
        int length = numArray.length;

        for(double num : numArray) {
            sum += num;
        }

        double mean = sum / length;

        for(double num : numArray) {
            standardDeviation += FastMath.pow(num - mean, 2);
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
     * Compute binary logarithm
     *
     * @param bits Input
     * @return Binary logarithm
     */
    public static int binlog(int bits) {
        int log = 0;
        if((bits & 0xffff0000) != 0) {
            bits >>>= 16;
            log = 16;
        }
        if(bits >= 256) {
            bits >>>= 8;
            log += 8;
        }
        if(bits >= 16) {
            bits >>>= 4;
            log += 4;
        }
        if(bits >= 4) {
            bits >>>= 2;
            log += 2;
        }
        return log + (bits >>> 1);
    }
}
