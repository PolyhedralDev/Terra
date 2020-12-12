package com.dfsek.terra.bukkit;

import com.dfsek.terra.api.generic.Tree;
import com.dfsek.terra.api.generic.generator.ChunkGenerator;
import com.dfsek.terra.api.generic.world.Chunk;
import com.dfsek.terra.api.generic.world.World;
import com.dfsek.terra.api.generic.world.block.Block;
import com.dfsek.terra.api.generic.world.vector.Location;
import com.dfsek.terra.bukkit.generator.BukkitChunkGenerator;
import com.dfsek.terra.bukkit.world.block.BukkitBlock;

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

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public UUID getUID() {
        return delegate.getUID();
    }

    @Override
    public boolean isChunkGenerated(int x, int z) {
        return delegate.isChunkGenerated(x, z);
    }

    @Override
    public Chunk getChunkAt(int x, int z) {
        return new BukkitChunk(delegate.getChunkAt(x, z));
    }

    @Override
    public File getWorldFolder() {
        return delegate.getWorldFolder();
    }

    @Override
    public Block getBlockAt(int x, int y, int z) {
        return new BukkitBlock(delegate.getBlockAt(x, y, z));
    }

    @Override
    public Block getBlockAt(Location l) {
        return new BukkitBlock(delegate.getBlockAt(l.getBlockX(), l.getBlockY(), l.getBlockZ()));
    }

    @Override
    public boolean generateTree(Location l, Tree vanillaTreeType) {
        return delegate.generateTree(new org.bukkit.Location(delegate, l.getX(), l.getY(), l.getZ()), ((BukkitTree) vanillaTreeType).getHandle());
    }

    @Override
    public void spawn(Location l, Class<com.dfsek.terra.api.generic.Entity> entity, java.util.function.Consumer<com.dfsek.terra.api.generic.Entity> consumer) {
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
}
