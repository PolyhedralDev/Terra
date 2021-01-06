package com.dfsek.terra.api.math.voxel;

import com.dfsek.terra.api.math.noise.FastNoiseLite;
import com.dfsek.terra.api.math.vector.Vector3;

public class DeformedSphere extends VoxelGeometry {
    public DeformedSphere(Vector3 start, int rad, double deform, FastNoiseLite noise) {
        for(int x = -rad; x <= rad; x++) {
            for(int y = -rad; y <= rad; y++) {
                for(int z = -rad; z <= rad; z++) {
                    Vector3 c = new Vector3(x, y, z);
                    if(c.length() < (rad + 0.5) * ((noise.getNoise(x, y, z) + 1) * deform)) {
                        addVector(c.add(start));
                    }
                }
            }
        }
    }
}
