package com.dfsek.terra.api.world;


import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.vector.Vector3;

import java.util.Random;
import java.util.Set;

public interface Tree {
    boolean plant(Vector3 l, World world, Random r);

    Set<BlockType> getSpawnable();
}
