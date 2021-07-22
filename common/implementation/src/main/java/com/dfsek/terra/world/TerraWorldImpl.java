package com.dfsek.terra.world;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.TerraWorld;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.config.pack.WorldConfigImpl;

public class TerraWorldImpl implements TerraWorld {
    private final WorldConfigImpl config;
    private final World world;

    public TerraWorldImpl(World w, ConfigPack c, TerraPlugin main) {
        if(!w.isTerraWorld()) throw new IllegalArgumentException("World " + w + " is not a Terra World!");
        this.world = w;
        config = (WorldConfigImpl) c.toWorldConfig(w);
    }


    @Override
    public World getWorld() {
        return world;
    }


    @Override
    public WorldConfigImpl getConfig() {
        return config;
    }


    @Override
    public BlockState getUngeneratedBlock(int x, int y, int z) {
        return world.getTerraGenerator().getBlock(world, x, y, z);
    }

    @Override
    public BlockState getUngeneratedBlock(Vector3 v) {
        return getUngeneratedBlock(v.getBlockX(), v.getBlockY(), v.getBlockZ());
    }
}
