package com.dfsek.terra.fabric.world.handles;

import com.dfsek.terra.api.generic.Entity;
import com.dfsek.terra.api.generic.Tree;
import com.dfsek.terra.api.generic.generator.ChunkGenerator;
import com.dfsek.terra.api.generic.world.Chunk;
import com.dfsek.terra.api.generic.world.World;
import com.dfsek.terra.api.generic.world.block.Block;
import com.dfsek.terra.api.generic.world.vector.Location;
import net.minecraft.world.ChunkRegion;

import java.io.File;
import java.util.UUID;
import java.util.function.Consumer;

public class FabricWorldChunkRegion implements World {
    private final ChunkRegion delegate;

    public FabricWorldChunkRegion(ChunkRegion delegate) {
        this.delegate = delegate;
    }

    @Override
    public long getSeed() {
        return delegate.getSeed();
    }

    @Override
    public int getMaxHeight() {
        return delegate.getHeight();
    }

    @Override
    public ChunkGenerator getGenerator() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public UUID getUID() {
        return null;
    }

    @Override
    public boolean isChunkGenerated(int x, int z) {
        return false;
    }

    @Override
    public Chunk getChunkAt(int x, int z) {
        return null;
    }

    @Override
    public File getWorldFolder() {
        return null;
    }

    @Override
    public Block getBlockAt(int x, int y, int z) {
        return null;
    }

    @Override
    public Block getBlockAt(Location l) {
        return null;
    }

    @Override
    public boolean generateTree(Location l, Tree vanillaTreeType) {
        return false;
    }

    @Override
    public void spawn(Location location, Class<Entity> entity, Consumer<Entity> consumer) {

    }

    @Override
    public Object getHandle() {
        return null;
    }
}
