package com.dfsek.terra.structure;

import com.dfsek.terra.config.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.util.Vector;
import org.polydev.gaea.math.MathUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StructureSpawn {
    private final int separation;
    private final int width;
    public StructureSpawn(int width, int separation) {
        this.separation = separation;
        this.width = width;
    }
    public Vector getNearestSpawn(int x, int z, long seed) {
        int structureChunkX = x / (width + 2*separation);
        int structureChunkZ = z / (width + 2*separation);
        List<Vector> zones = new ArrayList<>();
        for(int xi = structureChunkX-1; xi <= structureChunkX+1; xi++) {
            for(int zi = structureChunkZ-1; zi <= structureChunkZ+1; zi++) {
                zones.add(getStructureChunkSpawn(xi, zi, seed));
            }
        }
        Vector shortest = zones.get(0);
        Vector compare = new Vector(x, 0, z);
        for(Vector v : zones) {
            if(compare.distanceSquared(shortest) > compare.distanceSquared(v)) shortest = v.clone();
        }
        return shortest;
    }
    private Vector getStructureChunkSpawn(int structureChunkX, int structureChunkZ, long seed) {
        Random r = new Random(MathUtil.getCarverChunkSeed(structureChunkX, structureChunkZ, seed));
        int offsetX = r.nextInt(width);
        int offsetZ = r.nextInt(width);
        int sx = structureChunkX * (width + 2*separation) + offsetX;
        int sz = structureChunkZ * (width + 2*separation) + offsetZ;
        return new Vector(sx, 0, sz);
    }
}
