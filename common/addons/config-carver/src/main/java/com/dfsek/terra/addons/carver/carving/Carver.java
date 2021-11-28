/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.carver.carving;

import net.jafama.FastMath;

import java.util.Random;
import java.util.function.BiConsumer;

import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.access.World;


public abstract class Carver {
    private final int minY;
    private final int maxY;
    private final double sixtyFourSq = FastMath.pow(64, 2);
    private int carvingRadius = 4;
    
    public Carver(int minY, int maxY) {
        this.minY = minY;
        this.maxY = maxY;
    }
    
    public abstract void carve(int chunkX, int chunkZ, World w, BiConsumer<Vector3, CarvingType> consumer);
    
    public int getCarvingRadius() {
        return carvingRadius;
    }
    
    public void setCarvingRadius(int carvingRadius) {
        this.carvingRadius = carvingRadius;
    }
    
    public abstract Worm getWorm(long seed, Vector3 l);
    
    public abstract boolean isChunkCarved(World w, int chunkX, int chunkZ, Random r);
    
    public enum CarvingType {
        CENTER,
        WALL,
        TOP,
        BOTTOM
    }
}
