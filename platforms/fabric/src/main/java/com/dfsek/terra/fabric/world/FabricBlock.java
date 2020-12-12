package com.dfsek.terra.fabric.world;

import com.dfsek.terra.api.generic.world.block.Block;
import com.dfsek.terra.api.generic.world.block.BlockData;
import com.dfsek.terra.api.generic.world.block.BlockFace;
import com.dfsek.terra.api.generic.world.block.MaterialData;
import com.dfsek.terra.api.generic.world.vector.Location;

public class FabricBlock implements Block {
    private final BlockStorage delegate;

    public FabricBlock(BlockStorage block) {
        this.delegate = block;
    }

    @Override
    public void setBlockData(BlockData data, boolean physics) {
        delegate.getWorld().setBlockState(FabricAdapters.fromVector(delegate.getLocation().getVector()), ((FabricBlockData) data).getHandle(), 0, 0);
    }

    @Override
    public BlockData getBlockData() {
        return null;
    }

    @Override
    public Block getRelative(BlockFace face) {
        return null;
    }

    @Override
    public Block getRelative(BlockFace face, int len) {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Location getLocation() {
        return delegate.getLocation();
    }

    @Override
    public MaterialData getType() {
        return null;
    }

    @Override
    public int getX() {
        return delegate.getLocation().getBlockX();
    }

    @Override
    public int getZ() {
        return delegate.getLocation().getBlockZ();
    }

    @Override
    public int getY() {
        return delegate.getLocation().getBlockY();
    }

    @Override
    public boolean isPassable() {
        return false;
    }

    @Override
    public BlockStorage getHandle() {
        return delegate;
    }
}
