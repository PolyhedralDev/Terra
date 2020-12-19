package com.dfsek.terra.api.world.tree;


import com.dfsek.terra.api.platform.world.block.MaterialData;
import com.dfsek.terra.api.platform.world.vector.Location;

import java.util.Random;
import java.util.Set;

public interface Tree {
    boolean plant(Location l, Random r);

    Set<MaterialData> getSpawnable();
}
