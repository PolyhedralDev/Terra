package com.dfsek.terra.platform;

import com.dfsek.terra.api.util.collections.MaterialSet;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.Tree;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.vector.LocationImpl;

import java.util.Random;

public class RawTree implements Tree { // TODO: implement
    @Override
    public boolean plant(Vector3 l, World world, Random r) {
        return false;
    }

    @Override
    public MaterialSet getSpawnable() {
        return MaterialSet.empty();
    }
}
