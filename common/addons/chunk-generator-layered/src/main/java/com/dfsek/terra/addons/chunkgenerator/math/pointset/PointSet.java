package com.dfsek.terra.addons.chunkgenerator.math.pointset;

import java.util.function.Supplier;
import java.util.stream.Stream;

import com.dfsek.seismic.type.vector.Vector3Int;


public interface PointSet extends Supplier<Stream<Vector3Int>> {

    default Vector3Int[] toArray() {
        return this.get().distinct().toArray(Vector3Int[]::new);
    }
}
