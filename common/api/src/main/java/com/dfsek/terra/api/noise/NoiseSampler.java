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
            public double noise(long seed, double x, double y, List<double[]> context, int contextLayer, int contextRadius) {
                return 0;
            }
            
            @Override
            public double noise(long seed, double x, double y, double z, List<double[]> context, int contextLayer, int contextRadius) {
                return 0;
            }
            
            @Override
            public void generateContext(long seed, double x, double y, List<double[]> context, int contextLayer, int contextRadius) {
                //no-op
            }
            
            @Override
            public void generateContext(long seed, double x, double y, double z, List<double[]> context, int contextLayer,
                                            int contextRadius) {
                //no-op
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
    
    default double noise(Vector3 vector3, long seed, List<double[]> context, int contextLayer, int contextRadius) {
        return noise(seed, vector3.getX(), vector3.getY(), vector3.getZ(), context, contextLayer, contextRadius);
    }
    
    default double noise(Vector3Int vector3, long seed, List<double[]> context, int contextLayer, int contextRadius) {
        return noise(seed, vector3.getX(), vector3.getY(), vector3.getZ(), context, contextLayer, contextRadius);
    }
    
    
    default double noise(Vector2 vector2, long seed, List<double[]> context, int contextLayer, int contextRadius) {
        return noise(seed, vector2.getX(), vector2.getZ(), context, contextLayer, contextRadius);
    }
    
    default double noise(Vector2Int vector2, long seed, List<double[]> context, int contextLayer, int contextRadius) {
        return noise(seed, vector2.getX(), vector2.getZ(), context, contextLayer, contextRadius);
    }
    
    default double noise(long seed, double x, double y) {
        int contextRadius = getContextRadius();
        
        ArrayList<double[]> list = new ArrayList<>();
        generateContext(seed, x, y, list, 0, contextRadius);
        return noise(seed, x, y, list, 0, contextRadius);
    }
    
    default double noise(long seed, int x, int y) {
        return noise(seed, (double) x, y);
    }
    
    default double noise(long seed, double x, double y, double z) {
        int contextRadius = getContextRadius();
        
        ArrayList<double[]> list = new ArrayList<>();
        generateContext(seed, x, y, z, list, 0, contextRadius);
        return noise(seed, x, y, z, list, 0, contextRadius);
    }
    
    default double noise(long seed, int x, int y, int z) {
        return noise(seed, (double) x, y, z);
    }
    
    double noise(long seed, double x, double y, List<double[]> context, int contextLayer, int contextRadius);
    
    default double noise(long seed, int x, int y, List<double[]> context, int contextLayer, int contextRadius) {
        return noise(seed, (double) x, y, context, contextLayer, contextRadius);
    }
    
    double noise(long seed, double x, double y, double z, List<double[]> context, int contextLayer, int contextRadius);
    
    default double noise(long seed, int x, int y, int z, List<double[]> context, int contextLayer, int contextRadius) {
        return noise(seed, (double) x, y, z, context, contextLayer, contextRadius);
    }
    
    default void generateContext(long seed, double x, double y, List<double[]> context, int contextLayer, int contextRadius) {
        //no-op
    }
    default void generateContext(long seed, double x, double y, List<double[]> context) {
        generateContext(seed, x, y, context, 0, getContextRadius());
    }
    
    default void generateContext(long seed, int x, int y, List<double[]> context, int contextLayer, int contextRadius) {
        generateContext(seed, (double) x, y, context, contextLayer, contextRadius);
    }
    
    default void generateContext(long seed, int x, int y, List<double[]> context) {
        generateContext(seed, (double) x, y, context, 0, getContextRadius());
    }
    
    default void generateContext(long seed, double x, double y, double z, List<double[]> context, int contextLayer, int contextRadius) {
        //no-op
    }
    
    default void generateContext(long seed, double x, double y, double z, List<double[]> context) {
        generateContext(seed, x, y, z, context, 0, getContextRadius());
    }
    
    default void generateContext(long seed, int x, int y, int z, List<double[]> context, int contextLayer, int contextRadius) {
        generateContext(seed, (double) x, y, z, context, contextLayer, contextRadius);
    }
    
    default void generateContext(long seed, int x, int y, int z, List<double[]> context) {
        generateContext(seed, (double) x, y, z, context, 0, getContextRadius());
    }
    
    default int getContextRadius() {
        return 0;
    }
}
