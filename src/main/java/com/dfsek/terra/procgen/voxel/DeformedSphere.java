package com.dfsek.terra.procgen.voxel;

import com.dfsek.terra.api.gaea.math.FastNoiseLite;
import org.bukkit.util.Vector;

public class DeformedSphere extends VoxelGeometry {
    public DeformedSphere(Vector start, int rad, double deform, FastNoiseLite noise) {
        for(int x = -rad; x <= rad; x++) {
            for(int y = -rad; y <= rad; y++) {
                for(int z = -rad; z <= rad; z++) {
                    Vector c = new Vector(x, y, z);
                    if(c.length() < (rad + 0.5) * ((noise.getNoise(x, y, z) + 1) * deform)) {
                        addVector(c.add(start));
                    }
                }
            }
        }
    }
}
