package com.dfsek.terra.platform;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.block.MaterialData;
import com.dfsek.terra.api.world.tree.Tree;

import java.util.Collections;
import java.util.Random;
import java.util.Set;

public class RawTree implements Tree { // TODO: implement
    @Override
    public boolean plant(Location l, Random r) {
        return false;
    }

    @Override
    public Set<MaterialData> getSpawnable() {
        return Collections.emptySet();
    }
}
