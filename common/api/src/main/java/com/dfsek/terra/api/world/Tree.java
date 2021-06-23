package com.dfsek.terra.api.world;


import com.dfsek.terra.api.vector.LocationImpl;

import java.util.Random;

public interface Tree {
    boolean plant(LocationImpl l, Random r);

    MaterialSet getSpawnable();
}
