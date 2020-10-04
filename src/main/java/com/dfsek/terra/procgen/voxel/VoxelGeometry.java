package com.dfsek.terra.procgen.voxel;

import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public abstract class VoxelGeometry {
    public List<Vector> geometry = new ArrayList<>();

    public List<Vector> getGeometry() {
        return geometry;
    }

    protected void addVector(Vector v) {
        geometry.add(v);
    }

    public void merge(VoxelGeometry other) {
        geometry.addAll(other.getGeometry());
    }
    public static VoxelGeometry getBlank() {
        return new VoxelGeometry() {};
    }
}
