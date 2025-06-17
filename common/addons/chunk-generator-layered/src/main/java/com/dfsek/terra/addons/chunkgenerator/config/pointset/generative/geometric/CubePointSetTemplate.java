package com.dfsek.terra.addons.chunkgenerator.config.pointset.generative.geometric;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.chunkgenerator.math.pointset.PointSet;
import com.dfsek.terra.addons.chunkgenerator.math.pointset.generative.geometric.CuboidPointSet;


public class CubePointSetTemplate implements ObjectTemplate<PointSet> {
    
    @Value("size")
    private int size;
    
    @Override
    public PointSet get() {
        return new CuboidPointSet(-size, -size, -size, size, size, size);
    }
}
