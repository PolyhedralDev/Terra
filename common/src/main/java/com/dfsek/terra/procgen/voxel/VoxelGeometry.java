package com.dfsek.terra.procgen.voxel;

import com.dfsek.terra.api.gaea.util.GlueList;
import com.dfsek.terra.api.platform.world.vector.Vector3;

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
