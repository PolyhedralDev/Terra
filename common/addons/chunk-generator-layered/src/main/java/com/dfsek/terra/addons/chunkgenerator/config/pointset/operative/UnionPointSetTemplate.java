package com.dfsek.terra.addons.chunkgenerator.config.pointset.operative;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import java.util.List;

import com.dfsek.terra.addons.chunkgenerator.math.pointset.PointSet;
import com.dfsek.terra.addons.chunkgenerator.math.pointset.operative.UnionPointSet;


public class UnionPointSetTemplate implements ObjectTemplate<PointSet> {
    
    @Value("point-sets")
    private List<PointSet> sets;
    
    @Override
    public PointSet get() {
        return new UnionPointSet(sets);
    }
}
