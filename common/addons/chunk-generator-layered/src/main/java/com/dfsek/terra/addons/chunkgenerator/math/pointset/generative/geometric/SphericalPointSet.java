package com.dfsek.terra.addons.chunkgenerator.math.pointset.generative.geometric;

import java.util.stream.Stream;

import com.dfsek.terra.addons.chunkgenerator.math.pointset.PointSet;
import com.dfsek.seismic.type.vector.Vector3Int;
import com.dfsek.seismic.math.floatingpoint.FloatingPointFunctions;
import com.dfsek.seismic.type.DistanceFunction;


public class SphericalPointSet implements PointSet {
    
    private final Stream<Vector3Int> points;
    
    public SphericalPointSet(double radius) {
        Stream.Builder<Vector3Int> streamBuilder = Stream.builder();
        int roundedRadius = FloatingPointFunctions.ceil(radius);
        for(int x = -roundedRadius; x <= roundedRadius; x++) {
            for(int y = -roundedRadius; y <= roundedRadius; y++) {
                for(int z = -roundedRadius; z <= roundedRadius; z++) {
                    Vector3Int pos = Vector3Int.of(x, y, z);
                    double length = pos.toFloat().length(DistanceFunction.Euclidean);
                    if (length == 0) continue;
                    if (length > radius) continue;
                    streamBuilder.add(pos);
                }
            }
        }
        this.points = streamBuilder.build();
    }
    
    @Override
    public Stream<Vector3Int> get() {
        return points;
    }
}
