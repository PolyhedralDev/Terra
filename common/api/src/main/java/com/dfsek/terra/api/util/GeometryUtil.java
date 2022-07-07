package com.dfsek.terra.api.util;

import java.util.function.Consumer;

import com.dfsek.terra.api.util.vector.Vector3Int;


public final class GeometryUtil {
    private GeometryUtil() {

    }
    
    public static void sphere(Vector3Int origin, int radius, Consumer<Vector3Int> action) {
        for(int x = -radius; x <= radius; x++) {
            for(int y = -radius; y <= radius; y++) {
                for(int z = -radius; z <= radius; z++) {
                    if(x * x + y * y + z * z <= radius * radius) {
                        action.accept(Vector3Int.of(origin, x, y, z));
                    }
                }
            }
        }
    }
    
    public static void cube(Vector3Int origin, int radius, Consumer<Vector3Int> action) {
        for(int x = -radius; x <= radius; x++) {
            for(int y = -radius; y <= radius; y++) {
                for(int z = -radius; z <= radius; z++) {
                    action.accept(Vector3Int.of(origin, x, y, z));
                }
            }
        }
    }
}
