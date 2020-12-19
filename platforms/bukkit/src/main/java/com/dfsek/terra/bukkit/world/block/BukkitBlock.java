package com.dfsek.terra.bukkit.world.block;

import com.dfsek.terra.api.platform.world.block.Block;
import com.dfsek.terra.api.platform.world.block.BlockData;
import com.dfsek.terra.api.platform.world.block.BlockFace;
import com.dfsek.terra.api.platform.world.block.MaterialData;
import com.dfsek.terra.api.platform.world.vector.Location;
import com.dfsek.terra.bukkit.BukkitWorld;
import com.dfsek.terra.bukkit.world.block.data.TerraEnumAdapter;

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
        return new BukkitBlockData(delegate.getBlockData());
    }

    @Override
    public Block getRelative(BlockFace face) {
        return new BukkitBlock(delegate.getRelative(TerraEnumAdapter.fromTerraBlockFace(face)));
    }

    @Override
    public Block getRelative(BlockFace face, int len) {
        return new BukkitBlock(delegate.getRelative(TerraEnumAdapter.fromTerraBlockFace(face), len));
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
    public Object getHandle() {
        return delegate;
    }
}
