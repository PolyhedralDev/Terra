package com.dfsek.terra.internal.impl;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.util.collections.MaterialSet;
import com.dfsek.terra.api.world.tree.Tree;

import java.util.Random;

public class RegionTree implements Tree {
    private final String id;

    public RegionTree(String id) {
        this.id = id;
    }

    @Override
    public boolean plant(Location l, Random r) {
        return false;
    }

    @Override
    public MaterialSet getSpawnable() {
        return MaterialSet.empty();
    }
}
