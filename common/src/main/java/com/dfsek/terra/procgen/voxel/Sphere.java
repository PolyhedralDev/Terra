package com.dfsek.terra.procgen.voxel;

import com.dfsek.terra.api.platform.world.vector.Vector3;

public class Sphere extends VoxelGeometry {
    public Sphere(Vector3 start, int rad) {
        for(int x = -rad; x <= rad; x++) {
            for(int y = -rad; y <= rad; y++) {
                for(int z = -rad; z <= rad; z++) {
                    Vector3 c = new Vector3(x, y, z);
                    if(c.length() < rad + 0.5) {
                        addVector(c.add(start));
                    }
                }
            }
        }
    }
}
