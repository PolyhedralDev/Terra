/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.structure.feature;

import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.world.access.World;
import com.dfsek.terra.api.world.access.WorldAccess;


public interface Feature {
    Structure getStructure(WorldAccess world, int x, int y, int z);
    
    Distributor getDistributor();
    
    Locator getLocator();
}
