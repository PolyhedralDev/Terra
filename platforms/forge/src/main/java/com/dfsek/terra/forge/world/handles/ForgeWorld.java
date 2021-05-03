package com.dfsek.terra.forge.world.handles;

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
import com.dfsek.terra.forge.world.handles.chunk.ForgeChunk;
import com.dfsek.terra.forge.world.handles.world.ForgeWorldHandle;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.server.ServerWorld;

public class ForgeWorld implements World, ForgeWorldHandle {

    private final Handle delegate;

    public ForgeWorld(ServerWorld world, ChunkGenerator generator) {
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
    public Chunk getChunkAt(int x, int z) {
        return new ForgeChunk(delegate.world.getChunk(x, z));
    }

    @Override
    public Block getBlockAt(int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        return new ForgeBlock(pos, delegate.world);
    }

    @Override
    public int hashCode() {
        return ((IServerWorld) delegate.world).getLevel().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ForgeWorld)) return false;
        return ((IServerWorld) ((ForgeWorld) obj).delegate.world).getLevel().equals(((IServerWorld) delegate.world).getLevel());
    }

    @Override
    public Entity spawnEntity(Location location, EntityType entityType) {
        net.minecraft.entity.Entity entity = ForgeAdapter.adapt(entityType).create(delegate.world);
        entity.setPos(location.getX(), location.getY(), location.getZ());
        delegate.world.addFreshEntity(entity);
        return new ForgeEntity(entity);
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
    public ServerWorld getWorld() {
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
