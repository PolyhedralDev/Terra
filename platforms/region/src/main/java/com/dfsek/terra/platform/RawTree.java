package com.dfsek.terra.platform;

import com.dfsek.terra.vector.LocationImpl;
import com.dfsek.terra.api.world.Tree;
import com.dfsek.terra.api.util.collections.MaterialSet;

import java.util.Random;

public class RawTree implements Tree { // TODO: implement
    @Override
    public boolean plant(LocationImpl l, Random r) {
        return false;
    }

    @Override
    public MaterialSet getSpawnable() {
        return MaterialSet.empty();
    }
}
