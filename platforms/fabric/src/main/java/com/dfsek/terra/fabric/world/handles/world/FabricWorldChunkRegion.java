package com.dfsek.terra.fabric.world.handles.world;

import com.dfsek.terra.api.generic.Entity;
import com.dfsek.terra.api.generic.Tree;
import com.dfsek.terra.api.generic.generator.ChunkGenerator;
import com.dfsek.terra.api.generic.world.Chunk;
import com.dfsek.terra.api.generic.world.World;
import com.dfsek.terra.api.generic.world.block.Block;
import com.dfsek.terra.api.generic.world.vector.Location;
import com.dfsek.terra.fabric.world.block.FabricBlock;
import com.dfsek.terra.fabric.world.generator.FabricChunkGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;

import java.io.File;
import java.util.UUID;
import java.util.function.Consumer;

public class FabricWorldChunkRegion implements World {
    private final Handle delegate;

    public FabricWorldChunkRegion(ChunkRegion delegate, net.minecraft.world.gen.chunk.ChunkGenerator generator) {
        this.delegate = new Handle(delegate, generator);
    }

    @Override
    public long getSeed() {
        return delegate.getChunk().getSeed();
    }

    @Override
    public int getMaxHeight() {
        return delegate.getChunk().getHeight();
    }

    @Override
    public ChunkGenerator getGenerator() {
        return new FabricChunkGenerator(delegate.getGenerator());
    }

    @Override
    public String getName() {
        return delegate.chunk.toString();
    }

    @Override
    public UUID getUID() {
        return UUID.randomUUID();
    }

    @Override
    public boolean isChunkGenerated(int x, int z) {
        return delegate.chunk.isChunkLoaded(x, z);
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
        BlockPos pos = new BlockPos(x, y, z);
        return new FabricBlock(pos, delegate.chunk);
    }

    @Override
    public int hashCode() {
        return delegate.generator.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof FabricWorldChunkRegion)) return false;
        return ((FabricWorldChunkRegion) obj).delegate.generator.equals(delegate.generator);
    }

    @Override
    public Block getBlockAt(Location l) {
        return getBlockAt(l.getBlockX(), l.getBlockY(), l.getBlockZ());
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

    public static final class Handle {
        private final ChunkRegion chunk;
        private final net.minecraft.world.gen.chunk.ChunkGenerator generator;

        public Handle(ChunkRegion chunk, net.minecraft.world.gen.chunk.ChunkGenerator generator) {
            this.chunk = chunk;
            this.generator = generator;
        }

        public net.minecraft.world.gen.chunk.ChunkGenerator getGenerator() {
            return generator;
        }

        public ChunkRegion getChunk() {
            return chunk;
        }
    }
}
