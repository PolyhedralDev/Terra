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


public interface NoiseSampler {
    static NoiseSampler zero() {
        return new NoiseSampler() {
            @Override
            public double noise(long seed, double x, double y) {
                return 0;
            }
            
            @Override
            public double noise(long seed, double x, double y, double z) {
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
    
    double noise(long seed, double x, double y);
    
    default double noise(long seed, int x, int y) {
        return noise(seed, (double) x, y);
    }
    
    double noise(long seed, double x, double y, double z);
    
    default double noise(long seed, int x, int y, int z) {
        return noise(seed, (double) x, y, z);
    }
}
