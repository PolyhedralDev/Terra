package com.dfsek.terra.procgen.voxel;

import com.dfsek.terra.api.math.vector.Vector3;

public class Tube extends VoxelGeometry {
    public Tube(Vector3 start, Vector3 end, int radius) {
        Vector3 step = start.clone().subtract(end).normalize();
        Vector3 run = start.clone();
        int steps = (int) start.distance(end);
        for(int i = 0; i < steps; i++) {
            merge(new Sphere(run, radius));
            run.add(step);
        }
    }
}
