package com.dfsek.terra.addons.chunkgenerator.config.pointset.generative;

import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.chunkgenerator.math.pointset.PointSet;
import com.dfsek.terra.addons.chunkgenerator.math.pointset.generative.AdjacentPointSet;


public class AdjacentPointSetTemplate implements ObjectTemplate<PointSet> {
    
    @Override
    public PointSet get() {
        return new AdjacentPointSet();
    }
}
