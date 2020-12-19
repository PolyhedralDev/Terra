package com.dfsek.terra.fabric.world.handles;

import com.dfsek.terra.api.platform.Entity;
import com.dfsek.terra.api.platform.Tree;
import com.dfsek.terra.api.platform.generator.ChunkGenerator;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.platform.world.block.Block;
import com.dfsek.terra.api.platform.world.vector.Location;
import com.dfsek.terra.fabric.world.handles.chunk.FabricChunk;
import net.minecraft.server.world.ServerWorld;

import java.io.File;
import java.util.UUID;
import java.util.function.Consumer;

public class FabricWorld implements World {

    private final Handle delegate;

    public FabricWorld(ServerWorld world, ChunkGenerator generator) {
        this.delegate = new Handle(world, generator);
    }

    @Override
    public long getSeed() {
        return delegate.world.getSeed();
    }

    @Override
    public int getMaxHeight() {
        return delegate.world.getHeight();
    }

    @Override
    public ChunkGenerator getGenerator() {
        return delegate.generator;
    }

    @Override
    public String getName() {
        return delegate.world.worldProperties.getLevelName();
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
        return new FabricChunk(delegate.world.getChunk(x, z));
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
    public Handle getHandle() {
        return null;
    }

    private final class Handle {
        private final ServerWorld world;
        private final ChunkGenerator generator;

        private Handle(ServerWorld world, ChunkGenerator generator) {
            this.world = world;
            this.generator = generator;
        }
    }
}
