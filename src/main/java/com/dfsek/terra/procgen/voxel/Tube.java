package com.dfsek.terra.procgen.voxel;

import org.bukkit.util.Vector;

public class Tube extends VoxelGeometry {
    public Tube(Vector start, Vector end, int radius) {
        Vector step = start.clone().subtract(end).normalize();
        Vector run = start.clone();
        int steps = (int) start.distance(end);
        for(int i = 0; i < steps; i++) {
            merge(new Sphere(run, radius));
            run.add(step);
        }
    }
}
