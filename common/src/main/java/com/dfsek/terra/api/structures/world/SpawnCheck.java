package com.dfsek.terra.api.structures.world;

import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.world.World;

public abstract class SpawnCheck {
    protected final World world;
    protected final TerraPlugin main;

    protected SpawnCheck(World world, TerraPlugin main) {
        this.world = world;
        this.main = main;
    }

    public abstract boolean check(int x, int y, int z);

}
