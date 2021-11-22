/*
 * This file is part of Terra.
 *
 * Terra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Terra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Terra.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.dfsek.terra.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.dfsek.terra.api.util.PopulationUtil;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.util.FastRandom;


/**
 * Class to procedurally determine the spawn point of an object based on a grid with padding between cells.
 */
public class GridSpawn implements com.dfsek.terra.api.structure.StructureSpawn {
    private final int separation;
    private final int width;
    private final int salt;
    
    public GridSpawn(int width, int separation, int salt) {
        this.separation = separation;
        this.width = width;
        this.salt = salt;
    }
    
    @Override
    public Vector3 getNearestSpawn(int x, int z, long seed) {
        int structureChunkX = x / (width + 2 * separation);
        int structureChunkZ = z / (width + 2 * separation);
        List<Vector3> zones = new ArrayList<>(9);
        for(int xi = structureChunkX - 1; xi <= structureChunkX + 1; xi++) {
            for(int zi = structureChunkZ - 1; zi <= structureChunkZ + 1; zi++) {
                zones.add(getChunkSpawn(xi, zi, seed));
            }
        }
        Vector3 shortest = zones.get(0);
        Vector3 compare = new Vector3(x, 0, z);
        for(Vector3 v : zones) {
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
     *
     * @return Vector representing spawnpoint
     */
    public Vector3 getChunkSpawn(int structureChunkX, int structureChunkZ, long seed) {
        Random r = new FastRandom(PopulationUtil.getCarverChunkSeed(structureChunkX, structureChunkZ, seed + salt));
        int offsetX = r.nextInt(width);
        int offsetZ = r.nextInt(width);
        int sx = structureChunkX * (width + 2 * separation) + offsetX;
        int sz = structureChunkZ * (width + 2 * separation) + offsetZ;
        return new Vector3(sx, 0, sz);
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getSeparation() {
        return separation;
    }
}
