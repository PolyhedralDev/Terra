package com.dfsek.terra.addons.chunkgenerator.math.pointset.generative.geometric;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import com.dfsek.terra.addons.chunkgenerator.math.pointset.PointSet;
import com.dfsek.seismic.type.vector.Vector3Int;


public class CuboidPointSet implements PointSet {
    
    private final Stream<Vector3Int> points;
    
    public CuboidPointSet(int x1, int y1, int z1, int x2, int y2, int z2) {
        Set<Vector3Int> points = new HashSet<>();
        for (int x = x1; x <= x2; x = x + 1) {
            for (int y = y1; y <= y2; y = y + 1) {
                for (int z = z1; z <= z2; z = z + 1) {
                    points.add(Vector3Int.of(x, y, z));
                }
            }
        }
        this.points = points.stream();
    }
    
    @Override
    public Stream<Vector3Int> get() {
        return points;
    }
}
