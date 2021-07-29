package com.dfsek.terra.addons.generation.feature;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.world.Column;
import com.dfsek.terra.api.world.World;

public class ColumnImpl implements Column {
    private final int x;
    private final int z;
    private final World world;

    public ColumnImpl(int x, int z, World world) {
        this.x = x;
        this.z = z;
        this.world = world;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public BlockState getBlock(int y) {
        return world.getBlockData(x, y, z);
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public int getMinY() {
        return world.getMinHeight();
    }

    @Override
    public int getMaxY() {
        return world.getMaxHeight();
    }
}
