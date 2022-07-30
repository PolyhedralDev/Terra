package com.dfsek.terra.addons.chunkgenerator.math.pointset.generative;

import java.util.stream.Stream;

import com.dfsek.terra.addons.chunkgenerator.math.pointset.PointSet;
import com.dfsek.terra.api.util.vector.Vector3Int;

public class AdjacentPointSet implements PointSet {
    @Override
    public Stream<Vector3Int> get() {
        return Stream.of(
                Vector3Int.of(0, 0, -1),
                Vector3Int.of(0, 0, 1),
                Vector3Int.of(0, -1, 0),
                Vector3Int.of(0, 1, 0),
                Vector3Int.of(-1, 0, 0),
                Vector3Int.of(1, 0, 0)
        );
    }
}
