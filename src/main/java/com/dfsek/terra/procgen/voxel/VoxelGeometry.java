package com.dfsek.terra.procgen.voxel;

import com.dfsek.terra.util.GlueList;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public abstract class VoxelGeometry {
    private final List<Vector> geometry = new GlueList<>();

    public static VoxelGeometry getBlank() {
        return new VoxelGeometry() {
        };
    }

    public List<Vector> getGeometry() {
        return geometry;
    }

    protected void addVector(Vector v) {
        geometry.add(v);
    }

    public void merge(VoxelGeometry other) {
        geometry.addAll(other.geometry);
    }
}
