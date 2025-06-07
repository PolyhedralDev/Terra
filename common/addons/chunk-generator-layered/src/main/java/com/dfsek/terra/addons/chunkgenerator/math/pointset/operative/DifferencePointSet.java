package com.dfsek.terra.addons.chunkgenerator.math.pointset.operative;

import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.dfsek.terra.addons.chunkgenerator.math.pointset.PointSet;
import com.dfsek.seismic.type.vector.Vector3Int;

public class DifferencePointSet implements PointSet {
    
    private final Stream<Vector3Int> points;
    
    public DifferencePointSet(List<PointSet> sets) {
        points = sets.stream()
                     .map(PointSet::get)
                     .map(s -> s.collect(Collectors.toSet()))
                     .reduce(Sets::difference).orElse(Collections.emptySet())
                     .stream();
    }
    
    @Override
    public Stream<Vector3Int> get() {
        return points;
    }
}
