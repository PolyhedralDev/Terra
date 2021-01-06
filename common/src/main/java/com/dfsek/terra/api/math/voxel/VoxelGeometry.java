package com.dfsek.terra.api.math.voxel;

import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.util.GlueList;

import java.util.List;

public abstract class VoxelGeometry {
    private final List<Vector3> geometry = new GlueList<>();

    public static VoxelGeometry getBlank() {
        return new VoxelGeometry() {
        };
    }

    public List<Vector3> getGeometry() {
        return geometry;
    }

    protected void addVector(Vector3 v) {
        geometry.add(v);
    }

    public void merge(VoxelGeometry other) {
        geometry.addAll(other.geometry);
    }
}
