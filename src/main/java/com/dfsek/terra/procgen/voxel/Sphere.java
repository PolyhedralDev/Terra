package com.dfsek.terra.procgen.voxel;

import org.bukkit.util.Vector;

public class Sphere extends VoxelGeometry {
    public Sphere(Vector start, int rad) {
        for(int x = -rad; x <= rad; x++) {
            for(int y = -rad; y <= rad; y++) {
                for(int z = -rad; z <= rad; z++) {
                    Vector c = new Vector(x, y, z);
                    if(c.length() < rad + 0.5) {
                        addVector(c.add(start));
                    }
                }
            }
        }
    }
}
