package com.dfsek.terra.bukkit.world.block;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.BlockFace;
import com.dfsek.terra.api.platform.block.MaterialData;
import com.dfsek.terra.api.platform.block.state.BlockState;
import com.dfsek.terra.bukkit.BukkitWorld;
import com.dfsek.terra.bukkit.world.block.data.BukkitBlockData;
import com.dfsek.terra.bukkit.world.block.data.BukkitEnumAdapter;

public class BukkitBlock implements Block {
    private final org.bukkit.block.Block delegate;

    public BukkitBlock(org.bukkit.block.Block delegate) {
        this.delegate = delegate;
    }

    @Override
    public void setBlockData(BlockData data, boolean physics) {
        delegate.setBlockData(((BukkitBlockData) data).getHandle(), physics);
    }

    @Override
    public BlockData getBlockData() {
        return BukkitBlockData.newInstance(delegate.getBlockData());
    }

    @Override
    public BlockState getState() {
        return null;
    }

    @Override
    public Block getRelative(BlockFace face) {
        return new BukkitBlock(delegate.getRelative(BukkitEnumAdapter.adapt(face)));
    }

    @Override
    public Block getRelative(BlockFace face, int len) {
        return new BukkitBlock(delegate.getRelative(BukkitEnumAdapter.adapt(face), len));
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public Location getLocation() {
        return new Location(new BukkitWorld(delegate.getWorld()), delegate.getX(), delegate.getY(), delegate.getZ());
    }

    @Override
    public MaterialData getType() {
        return new BukkitMaterialData(delegate.getType());
    }

    @Override
    public int getX() {
        return delegate.getX();
    }

    @Override
    public int getZ() {
        return delegate.getZ();
    }

    @Override
    public int getY() {
        return delegate.getY();
    }

    @Override
    public boolean isPassable() {
        return delegate.isPassable();
    }

    @Override
    public org.bukkit.block.Block getHandle() {
        return delegate;
    }
}
