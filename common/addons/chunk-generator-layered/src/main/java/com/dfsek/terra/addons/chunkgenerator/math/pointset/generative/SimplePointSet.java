package com.dfsek.terra.addons.chunkgenerator.math.pointset.generative;

import java.util.Set;
import java.util.stream.Stream;

import com.dfsek.terra.addons.chunkgenerator.math.pointset.PointSet;
import com.dfsek.seismic.type.vector.Vector3Int;


public class SimplePointSet implements PointSet {
    
    private final Stream<Vector3Int> points;
    
    public SimplePointSet(Set<Vector3Int> points) {
        this.points = points.stream();
    }
    
    @Override
    public Stream<Vector3Int> get() {
        return points;
    }
}
