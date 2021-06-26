package com.dfsek.terra.bukkit.world;

import com.dfsek.terra.api.block.BlockState;
import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.generator.ChunkGenerator;
import com.dfsek.terra.bukkit.BukkitEntity;
import com.dfsek.terra.bukkit.generator.BukkitChunkGenerator;
import com.dfsek.terra.bukkit.world.block.state.BukkitBlockEntity;
import com.dfsek.terra.bukkit.world.entity.BukkitEntityType;

import java.io.File;
import java.util.UUID;

public class BukkitWorld implements World {
    private final org.bukkit.World delegate;

    public BukkitWorld(org.bukkit.World delegate) {
        this.delegate = delegate;
    }

    @Override
    public long getSeed() {
        return delegate.getSeed();
    }

    @Override
    public int getMaxHeight() {
        return delegate.getMaxHeight();
    }

    @Override
    public ChunkGenerator getGenerator() {
        return new BukkitChunkGenerator(delegate.getGenerator());
    }

    public String getName() {
        return delegate.getName();
    }

    public UUID getUID() {
        return delegate.getUID();
    }

    public boolean isChunkGenerated(int x, int z) {
        return delegate.isChunkGenerated(x, z);
    }

    @Override
    public Chunk getChunkAt(int x, int z) {
        return BukkitAdapter.adapt(delegate.getChunkAt(x, z));
    }

    @Override
    public BlockState getBlockData(int x, int y, int z) {
        return BukkitAdapter.adapt(delegate.getBlockAt(x, y, z).getBlockData());
    }

    @Override
    public void setBlockData(int x, int y, int z, BlockState data, boolean physics) {
        delegate.getBlockAt(x, y, z).setBlockData(BukkitAdapter.adapt(data), physics);
    }

    @Override
    public BlockEntity getBlockState(int x, int y, int z) {
        return BukkitBlockEntity.newInstance(delegate.getBlockAt(x, y, z).getState());
    }

    public File getWorldFolder() {
        return delegate.getWorldFolder();
    }

    @Override
    public Entity spawnEntity(Vector3 location, EntityType entityType) {
        return new BukkitEntity(delegate.spawnEntity(BukkitAdapter.adapt(location).toLocation(delegate), ((BukkitEntityType) entityType).getHandle()));
    }

    @Override
    public int getMinHeight() {
        return delegate.getMinHeight();
    }

    @Override
    public org.bukkit.World getHandle() {
        return delegate;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof BukkitWorld)) return false;
        BukkitWorld other = (BukkitWorld) obj;
        return other.getHandle().equals(delegate);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public String toString() {
        return delegate.toString();
    }
}
