package com.dfsek.terra.api.structure.feature;

import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.world.World;


public interface Feature {
    Structure getStructure(World world, int x, int y, int z);
    
    Distributor getDistributor();
    
    Locator getLocator();
}
