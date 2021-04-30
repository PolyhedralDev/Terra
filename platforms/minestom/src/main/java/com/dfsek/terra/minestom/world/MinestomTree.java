package com.dfsek.terra.minestom.world;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.world.Tree;
import com.dfsek.terra.api.util.collections.MaterialSet;

import java.util.Random;

public class MinestomTree implements Tree {
    @Override
    public boolean plant(Location l, Random r) {
        return true;
    }

    @Override
    public MaterialSet getSpawnable() {
        return MaterialSet.empty();
    }
}
