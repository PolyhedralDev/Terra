package com.dfsek.terra.api.structures.world;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.world.World;

public class LandCheck extends SpawnCheck {
    public LandCheck(World world, TerraPlugin main) {
        super(world, main);
    }

    @Override
    public boolean check(int x, int y, int z) {
        TerraWorld tw = main.getWorld(world);
        return sample(x, y, z, tw.getGrid()) > 0;
    }
}
