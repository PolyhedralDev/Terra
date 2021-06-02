package com.dfsek.terra.sponge.world;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.world.Tree;
import com.dfsek.terra.api.util.collections.MaterialSet;

import java.util.Random;

public class SpongeTree implements Tree {
    @Override
    public boolean plant(Location l, Random r) {
        return false;
    }

    @Override
    public MaterialSet getSpawnable() {
        return MaterialSet.empty();
    }
}
