package com.dfsek.terra.api.world;


import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.vector.Location;

import java.util.Random;
import java.util.Set;

public interface Tree {
    boolean plant(Location l, Random r);

    Set<BlockType> getSpawnable();
}
