/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.noise;


import com.dfsek.terra.api.util.vector.Vector2;
import com.dfsek.terra.api.util.vector.Vector2Int;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.util.vector.Vector3Int;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public interface NoiseSampler {
    static NoiseSampler zero() {
        return new NoiseSampler() {
            @Override
            public double noise(long seed, double x, double y, double[] context, int contextRadius) {
                return 0;
            }
            
            @Override
            public double noise(long seed, double x, double y, double z, double[] context, int contextRadius) {
                return 0;
            }

            @Override
            public int getContextRadius() {
                return 0;
            }
        };
    }
    
    default double noise(Vector3 vector3, long seed) {
        return noise(seed, vector3.getX(), vector3.getY(), vector3.getZ());
    }
    
    default double noise(Vector3Int vector3, long seed) {
        return noise(seed, vector3.getX(), vector3.getY(), vector3.getZ());
    }
    
    
    default double noise(Vector2 vector2, long seed) {
        return noise(seed, vector2.getX(), vector2.getZ());
    }
    
    default double noise(Vector2Int vector2, long seed) {
        return noise(seed, vector2.getX(), vector2.getZ());
    }
    
    default double noise(Vector3 vector3, long seed, double[] context, int contextRadius) {
        return noise(seed, vector3.getX(), vector3.getY(), vector3.getZ(), context, contextRadius);
    }
    
    default double noise(Vector3Int vector3, long seed, double[] context, int contextRadius) {
        return noise(seed, vector3.getX(), vector3.getY(), vector3.getZ(), context, contextRadius);
    }
    
    
    default double noise(Vector2 vector2, long seed, double[] context, int contextRadius) {
        return noise(seed, vector2.getX(), vector2.getZ(), context, contextRadius);
    }
    
    default double noise(Vector2Int vector2, long seed, double[] context, int contextRadius) {
        return noise(seed, vector2.getX(), vector2.getZ(), context, contextRadius);
    }
    
    default double noise(long seed, double x, double y) {
        int contextRadius = getContextRadius();
        
        double[] context = generateContext(seed, x, y, contextRadius);
        return noise(seed, x, y, context, contextRadius);
    }
    
    default double noise(long seed, int x, int y) {
        return noise(seed, (double) x, y);
    }
    
    default double noise(long seed, double x, double y, double z) {
        int contextRadius = getContextRadius();
        
        double[] context = generateContext(seed, x, y, z, contextRadius);
        return noise(seed, x, y, z, context, contextRadius);
    }
    
    default double noise(long seed, int x, int y, int z) {
        return noise(seed, (double) x, y, z);
    }
    
    double noise(long seed, double x, double y, double[] context, int contextRadius);
    
    default double noise(long seed, int x, int y, double[] context, int contextRadius) {
        return noise(seed, (double) x, y, context, contextRadius);
    }
    
    double noise(long seed, double x, double y, double z, double[] context, int contextRadius);
    
    default double noise(long seed, int x, int y, int z, double[] context, int contextRadius) {
        return noise(seed, (double) x, y, z, context, contextRadius);
    }
    
    default double[] generateContext(long seed, Vector2 vector2, int contextRadius) {
        return new double[0];
    }
    default double[] generateContext(long seed, Vector2 vector2) {
        return generateContext(seed, vector2.getX(), vector2.getZ());
    }
    
    default double[] generateContext(long seed, Vector2Int vector2, int contextRadius) {
        return generateContext(seed, (double) vector2.getX(), vector2.getZ(), contextRadius);
    }
    
    default double[] generateContext(long seed, Vector2Int vector2) {
        return generateContext(seed, (double) vector2.getX(), vector2.getZ());
    }
    
    default double[] generateContext(long seed, Vector3 vector3, int contextRadius) {
        return new double[0];
    }
    
    default double[] generateContext(long seed, Vector3 vector3) {
        return generateContext(seed, vector3.getX(), vector3.getY(), vector3.getZ());
    }
    
    default double[] generateContext(long seed, Vector3Int vector3, int contextRadius) {
        return generateContext(seed, (double) vector3.getX(), vector3.getY(), vector3.getZ(), contextRadius);
    }
    
    default double[] generateContext(long seed, Vector3Int vector3) {
        return generateContext(seed, (double) vector3.getX(), vector3.getY(), vector3.getZ());
    }
    
    default double[] generateContext(long seed, double x, double y, int contextRadius) {
        return new double[0];
    }
    default double[] generateContext(long seed, double x, double y) {
        return generateContext(seed, x, y, getContextRadius());
    }
    
    default double[] generateContext(long seed, int x, int y) {
        return generateContext(seed, (double) x, y);
    }
    
    default double[] generateContext(long seed, double x, double y, double z, int contextRadius) {
        return new double[0];
    }
    
    default double[] generateContext(long seed, double x, double y, double z) {
        return generateContext(seed, x, y, z, getContextRadius());
    }
    
    default double[] generateContext(long seed, int x, int y, int z, int contextRadius) {
        return generateContext(seed, (double) x, y, z, contextRadius);
    }
    
    default double[] generateContext(long seed, int x, int y, int z) {
        return generateContext(seed, (double) x, y, z);
    }
    
    default int getContextRadius() {
        return 0;
    }
}
