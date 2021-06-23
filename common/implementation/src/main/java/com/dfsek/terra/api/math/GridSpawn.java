package com.dfsek.terra.api.math;

import com.dfsek.terra.api.util.MathUtil;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.vector.Vector3Impl;
import com.dfsek.terra.api.util.FastRandom;
import com.dfsek.terra.api.util.GlueList;

import java.util.List;
import java.util.Random;

/**
 * Class to procedurally determine the spawn point of an object based on a grid with padding between cells.
 */
public class GridSpawn {
    private final int separation;
    private final int width;
    private final int salt;

    public GridSpawn(int width, int separation, int salt) {
        this.separation = separation;
        this.width = width;
        this.salt = salt;
    }

    /**
     * Get nearest spawn point
     *
     * @param x    X coordinate
     * @param z    Z coordinate
     * @param seed Seed for RNG
     * @return Vector representing nearest spawnpoint
     */
    public Vector3 getNearestSpawn(int x, int z, long seed) {
        int structureChunkX = x / (width + 2 * separation);
        int structureChunkZ = z / (width + 2 * separation);
        List<Vector3> zones = new GlueList<>();
        for(int xi = structureChunkX - 1; xi <= structureChunkX + 1; xi++) {
            for(int zi = structureChunkZ - 1; zi <= structureChunkZ + 1; zi++) {
                zones.add(getChunkSpawn(xi, zi, seed));
            }
        }
        Vector3Impl shortest = zones.get(0);
        Vector3 compare = new Vector3Impl(x, 0, z);
        for(Vector3Impl v : zones) {
            if(compare.distanceSquared(shortest) > compare.distanceSquared(v)) shortest = v.clone();
        }
        return shortest;
    }

    /**
     * Get the X/Z coordinates of the spawn point in the nearest Chunk (not Minecraft chunk)
     *
     * @param structureChunkX Chunk X coordinate
     * @param structureChunkZ Chunk Z coordinate
     * @param seed            Seed for RNG
     * @return Vector representing spawnpoint
     */
    public Vector3 getChunkSpawn(int structureChunkX, int structureChunkZ, long seed) {
        Random r = new FastRandom(MathUtil.getCarverChunkSeed(structureChunkX, structureChunkZ, seed + salt));
        int offsetX = r.nextInt(width);
        int offsetZ = r.nextInt(width);
        int sx = structureChunkX * (width + 2 * separation) + offsetX;
        int sz = structureChunkZ * (width + 2 * separation) + offsetZ;
        return new Vector3Impl(sx, 0, sz);
    }

    public int getWidth() {
        return width;
    }

    public int getSeparation() {
        return separation;
    }
}
