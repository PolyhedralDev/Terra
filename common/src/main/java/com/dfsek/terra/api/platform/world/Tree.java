package com.dfsek.terra.api.platform.world;


import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.util.collections.MaterialSet;

import java.util.Random;

public interface Tree {
    boolean plant(Location l, Random r);

    MaterialSet getSpawnable();
}
