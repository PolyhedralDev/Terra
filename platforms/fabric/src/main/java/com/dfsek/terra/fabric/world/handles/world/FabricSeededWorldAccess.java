package com.dfsek.terra.fabric.world.handles.world;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.generator.ChunkGenerator;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.Tree;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.platform.world.entity.Entity;
import com.dfsek.terra.api.platform.world.entity.EntityType;
import com.dfsek.terra.fabric.world.block.FabricBlock;
import com.dfsek.terra.fabric.world.generator.FabricChunkGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

import java.io.File;
import java.util.UUID;

public class FabricSeededWorldAccess implements World {

    private final Handle handle;

    public FabricSeededWorldAccess(WorldAccess access, long seed, net.minecraft.world.gen.chunk.ChunkGenerator generator) {
        this.handle = new Handle(access, seed, generator);
    }

    @Override
    public long getSeed() {
        return handle.getSeed();
    }

    @Override
    public int getMaxHeight() {
        return handle.getWorldAccess().getDimensionHeight();
    }

    @Override
    public ChunkGenerator getGenerator() {
        return new FabricChunkGenerator(handle.getGenerator());
    }

    @Override
    public String getName() {
        return handle.toString(); // TODO: implementation
    }

    @Override
    public UUID getUID() {
        return UUID.randomUUID(); // TODO: implementation
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
        BlockPos pos = new BlockPos(x, y, z);
        return new FabricBlock(pos, handle.worldAccess);
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
    public Entity spawnEntity(Location location, EntityType entityType) {
        return null;
    }

    @Override
    public int hashCode() {
        return handle.worldAccess.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public Handle getHandle() {
        return handle;
    }

    public static class Handle {
        private final WorldAccess worldAccess;
        private final long seed;
        private final net.minecraft.world.gen.chunk.ChunkGenerator generator;

        public Handle(WorldAccess worldAccess, long seed, net.minecraft.world.gen.chunk.ChunkGenerator generator) {
            this.worldAccess = worldAccess;
            this.seed = seed;
            this.generator = generator;
        }

        public net.minecraft.world.gen.chunk.ChunkGenerator getGenerator() {
            return generator;
        }

        public long getSeed() {
            return seed;
        }

        public WorldAccess getWorldAccess() {
            return worldAccess;
        }
    }
}
