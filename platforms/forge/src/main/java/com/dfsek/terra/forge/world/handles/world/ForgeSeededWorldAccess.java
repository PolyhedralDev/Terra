package com.dfsek.terra.forge.world.handles.world;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.entity.Entity;
import com.dfsek.terra.api.platform.entity.EntityType;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.platform.world.generator.ChunkGenerator;
import com.dfsek.terra.forge.world.ForgeAdapter;
import com.dfsek.terra.forge.world.block.ForgeBlock;
import com.dfsek.terra.forge.world.entity.ForgeEntity;
import com.dfsek.terra.forge.world.generator.ForgeChunkGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;

import java.io.File;
import java.util.UUID;

public class ForgeSeededWorldAccess implements World, ForgeWorldHandle {

    private final Handle handle;

    public ForgeSeededWorldAccess(IWorld access, long seed, net.minecraft.world.gen.ChunkGenerator generator) {
        this.handle = new Handle(access, seed, generator);
    }

    @Override
    public long getSeed() {
        return handle.getSeed();
    }

    @Override
    public int getMaxHeight() {
        return handle.getWorldAccess().getMaxBuildHeight();
    }

    @Override
    public ChunkGenerator getGenerator() {
        return new ForgeChunkGenerator(handle.getGenerator());
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
        return new ForgeBlock(pos, handle.worldAccess);
    }

    @Override
    public Entity spawnEntity(Location location, EntityType entityType) {
        net.minecraft.entity.Entity entity = ForgeAdapter.adapt(entityType).create((ServerWorld) handle.worldAccess);
        entity.setPos(location.getX(), location.getY(), location.getZ());
        handle.worldAccess.addFreshEntity(entity);
        return new ForgeEntity(entity);
    }

    @Override
    public int getMinHeight() {
        return 0;
    }

    @Override
    public int hashCode() {
        return ((IServerWorld) handle.worldAccess).getLevel().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ForgeSeededWorldAccess)) return false;
        return ((IServerWorld) ((ForgeSeededWorldAccess) obj).handle.worldAccess).getLevel().equals(((IServerWorld) handle.worldAccess).getLevel());
    }

    @Override
    public Handle getHandle() {
        return handle;
    }

    @Override
    public IWorld getWorld() {
        return handle.worldAccess;
    }

    public static class Handle {
        private final IWorld worldAccess;
        private final long seed;
        private final net.minecraft.world.gen.ChunkGenerator generator;

        public Handle(IWorld worldAccess, long seed, net.minecraft.world.gen.ChunkGenerator generator) {
            this.worldAccess = worldAccess;
            this.seed = seed;
            this.generator = generator;
        }

        public net.minecraft.world.gen.ChunkGenerator getGenerator() {
            return generator;
        }

        public long getSeed() {
            return seed;
        }

        public IWorld getWorldAccess() {
            return worldAccess;
        }
    }
}
