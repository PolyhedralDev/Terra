package com.dfsek.terra.addons.chunkgenerator.math.pointset.generative.geometric;

import net.jafama.FastMath;

import java.util.stream.Stream;

import com.dfsek.terra.addons.chunkgenerator.math.pointset.PointSet;
import com.dfsek.terra.api.util.vector.Vector3Int;


public class SphericalPointSet implements PointSet {
    
    private final Stream<Vector3Int> points;
    
    public SphericalPointSet(double radius) {
        Stream.Builder<Vector3Int> streamBuilder = Stream.builder();
        int roundedRadius = FastMath.ceilToInt(radius);
        for(int x = -roundedRadius; x <= roundedRadius; x++) {
            for(int y = -roundedRadius; y <= roundedRadius; y++) {
                for(int z = -roundedRadius; z <= roundedRadius; z++) {
                    Vector3Int pos = Vector3Int.of(x, y, z);
                    double length = pos.toVector3().length();
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
