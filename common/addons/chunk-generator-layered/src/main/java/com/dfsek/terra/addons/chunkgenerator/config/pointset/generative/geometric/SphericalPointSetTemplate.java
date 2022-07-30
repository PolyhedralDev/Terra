package com.dfsek.terra.addons.chunkgenerator.config.pointset.generative.geometric;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.chunkgenerator.math.pointset.PointSet;
import com.dfsek.terra.addons.chunkgenerator.math.pointset.generative.geometric.SphericalPointSet;


public class SphericalPointSetTemplate implements ObjectTemplate<PointSet> {
    
    @Value("radius")
    private double radius;
    
    @Override
    public PointSet get() {
        return new SphericalPointSet(radius);
    }
}
