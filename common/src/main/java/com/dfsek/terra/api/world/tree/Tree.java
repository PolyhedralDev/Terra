package com.dfsek.terra.api.world.tree;


import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.block.MaterialData;

import java.util.Random;
import java.util.Set;

public interface Tree {
    boolean plant(Location l, Random r);

    Set<MaterialData> getSpawnable();
}
