package com.dfsek.terra.fabric.world.handles;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.entity.Entity;
import com.dfsek.terra.api.platform.entity.EntityType;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.platform.world.generator.ChunkGenerator;
import com.dfsek.terra.fabric.world.FabricAdapter;
import com.dfsek.terra.fabric.world.block.FabricBlock;
import com.dfsek.terra.fabric.world.entity.FabricEntity;
import com.dfsek.terra.fabric.world.handles.chunk.FabricChunk;
import com.dfsek.terra.fabric.world.handles.world.FabricWorldHandle;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.WorldAccess;

import java.io.File;
import java.util.UUID;

public class FabricWorld implements World, FabricWorldHandle {

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
        BlockPos pos = new BlockPos(x, y, z);
        return new FabricBlock(pos, delegate.world);
    }

    @Override
    public int hashCode() {
        return ((ServerWorldAccess) delegate.world).toServerWorld().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof FabricWorld)) return false;
        return ((ServerWorldAccess) ((FabricWorld) obj).delegate.world).toServerWorld().equals(((ServerWorldAccess) delegate.world).toServerWorld());
    }

    @Override
    public Block getBlockAt(Location l) {
        return getBlockAt(l.getBlockX(), l.getBlockY(), l.getBlockZ());
    }

    @Override
    public Entity spawnEntity(Location location, EntityType entityType) {
        net.minecraft.entity.Entity entity = FabricAdapter.adapt(entityType).create(delegate.world);
        entity.setPos(location.getX(), location.getY(), location.getZ());
        delegate.world.spawnEntity(entity);
        return new FabricEntity(entity);
    }

    @Override
    public int getMinHeight() {
        return 0;
    }

    @Override
    public Handle getHandle() {
        return null;
    }

    @Override
    public WorldAccess getWorld() {
        return delegate.getWorld();
    }

    public static final class Handle {
        private final ServerWorld world;
        private final ChunkGenerator generator;

        private Handle(ServerWorld world, ChunkGenerator generator) {
            this.world = world;
            this.generator = generator;
        }

        public ChunkGenerator getGenerator() {
            return generator;
        }

        public ServerWorld getWorld() {
            return world;
        }
    }
}
