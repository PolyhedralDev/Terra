package com.dfsek.terra.api.world;


import com.dfsek.terra.api.vector.Location;

import java.util.Random;

public interface Tree {
    boolean plant(Location l, Random r);

    MaterialSet getSpawnable();
}
