package com.dfsek.terra.procgen.voxel;

import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import org.polydev.gaea.math.FastNoiseLite;

public class DeformedSphere extends VoxelGeometry {
    public DeformedSphere(Vector start, int rad, double deform, FastNoiseLite noise) {
        for(int x = -rad; x <= rad; x++) {
            for(int y = -rad; y <= rad; y++) {
                for(int z = -rad; z <= rad; z++) {
                    Vector c = new Vector(x, y, z);
                    if(c.length() < (rad + 0.5) * ((noise.getNoise(x, y, z)+1)*deform)) {
                        addVector(c.add(start));
                    }
                }
            }
        }
    }
}
