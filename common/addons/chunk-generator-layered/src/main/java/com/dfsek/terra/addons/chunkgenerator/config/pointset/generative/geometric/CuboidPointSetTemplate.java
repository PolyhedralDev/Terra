package com.dfsek.terra.addons.chunkgenerator.config.pointset.generative.geometric;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.chunkgenerator.math.pointset.PointSet;
import com.dfsek.terra.addons.chunkgenerator.math.pointset.generative.geometric.CuboidPointSet;


public class CuboidPointSetTemplate implements ObjectTemplate<PointSet> {
    
    @Value("size.x")
    private int xSize;
    
    @Value("size.y")
    private int ySize;
    
    @Value("size.z")
    private int zSize;
    
    @Override
    public PointSet get() {
        return new CuboidPointSet(-xSize, -ySize, -zSize, xSize, ySize, zSize);
    }
}
