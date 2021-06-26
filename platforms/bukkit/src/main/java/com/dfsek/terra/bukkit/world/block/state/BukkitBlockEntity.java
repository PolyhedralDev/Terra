package com.dfsek.terra.bukkit.world.block.state;

import com.dfsek.terra.api.block.BlockData;
import com.dfsek.terra.api.block.state.BlockEntity;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import com.dfsek.terra.bukkit.world.block.data.BukkitBlockData;
import org.bukkit.block.Container;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.Sign;

public class BukkitBlockEntity implements BlockEntity {
    private final org.bukkit.block.BlockState delegate;

    protected BukkitBlockEntity(org.bukkit.block.BlockState block) {
        this.delegate = block;
    }

    public static BukkitBlockEntity newInstance(org.bukkit.block.BlockState block) {
        if(block instanceof Container) return new BukkitContainer((Container) block);
        if(block instanceof Sign) return new BukkitSign((Sign) block);
        if(block instanceof CreatureSpawner) return new BukkitMobSpawner((CreatureSpawner) block);
        return new BukkitBlockEntity(block);
    }

    @Override
    public org.bukkit.block.BlockState getHandle() {
        return delegate;
    }

    @Override
    public Vector3 getPosition() {
        return BukkitAdapter.adapt(delegate.getBlock().getLocation().toVector());
    }

    @Override
    public int getX() {
        return delegate.getX();
    }

    @Override
    public int getY() {
        return delegate.getY();
    }

    @Override
    public int getZ() {
        return delegate.getZ();
    }

    @Override
    public BlockData getBlockData() {
        return BukkitBlockData.newInstance(delegate.getBlockData());
    }

    @Override
    public boolean update(boolean applyPhysics) {
        return delegate.update(true, applyPhysics);
    }
}
