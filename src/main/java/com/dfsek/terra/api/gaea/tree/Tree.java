package com.dfsek.terra.api.gaea.tree;


import com.dfsek.terra.api.generic.world.block.MaterialData;
import com.dfsek.terra.api.generic.world.vector.Location;

import java.util.Random;
import java.util.Set;

public interface Tree {
    boolean plant(Location l, Random r);

    Set<MaterialData> getSpawnable();
}
