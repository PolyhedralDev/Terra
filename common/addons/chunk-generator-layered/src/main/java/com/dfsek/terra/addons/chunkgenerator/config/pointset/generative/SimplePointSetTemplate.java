package com.dfsek.terra.addons.chunkgenerator.config.pointset.generative;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import java.util.Set;

import com.dfsek.terra.addons.chunkgenerator.math.pointset.PointSet;
import com.dfsek.terra.addons.chunkgenerator.math.pointset.generative.SimplePointSet;
import com.dfsek.terra.api.util.vector.Vector3Int;


public class SimplePointSetTemplate implements ObjectTemplate<PointSet> {
    
    @Value("points")
    private Set<Vector3Int> list;
    
    @Override
    public PointSet get() {
        return new SimplePointSet(list);
    }
}
