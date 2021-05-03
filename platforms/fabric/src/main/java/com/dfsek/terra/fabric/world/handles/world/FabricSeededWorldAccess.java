package com.dfsek.terra.fabric.world.handles.world;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.entity.Entity;
import com.dfsek.terra.api.platform.entity.EntityType;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.platform.world.generator.ChunkGenerator;
import com.dfsek.terra.fabric.world.FabricAdapter;
import com.dfsek.terra.fabric.world.block.FabricBlock;
import com.dfsek.terra.fabric.world.generator.FabricChunkGenerator;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.WorldAccess;

public class FabricSeededWorldAccess implements World, FabricWorldHandle {

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
    public Chunk getChunkAt(int x, int z) {
        return null;
    }

    @Override
    public Block getBlockAt(int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        return new FabricBlock(pos, handle.worldAccess);
    }

    @Override
    public Entity spawnEntity(Location location, EntityType entityType) {
        net.minecraft.entity.Entity entity = FabricAdapter.adapt(entityType).create((ServerWorld) handle.worldAccess);
        entity.setPos(location.getX(), location.getY(), location.getZ());
        handle.worldAccess.spawnEntity(entity);
        return (Entity) entity;
    }

    @Override
    public int getMinHeight() {
        return 0;
    }

    @Override
    public int hashCode() {
        return ((ServerWorldAccess) handle.worldAccess).toServerWorld().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof FabricSeededWorldAccess)) return false;
        return ((ServerWorldAccess) ((FabricSeededWorldAccess) obj).handle.worldAccess).toServerWorld().equals(((ServerWorldAccess) handle.worldAccess).toServerWorld());
    }

    @Override
    public Handle getHandle() {
        return handle;
    }

    @Override
    public WorldAccess getWorld() {
        return handle.worldAccess;
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
