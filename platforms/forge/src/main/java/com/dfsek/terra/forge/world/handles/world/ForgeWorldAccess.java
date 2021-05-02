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
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;

public class ForgeWorldAccess implements World, ForgeWorldHandle {
    private final IWorld delegate;

    public ForgeWorldAccess(IWorld delegate) {
        this.delegate = delegate;
    }

    @Override
    public long getSeed() {
        return ((ISeedReader) delegate).getSeed();
    }

    @Override
    public int getMaxHeight() {
        return delegate.getHeight();
    }

    @Override
    public ChunkGenerator getGenerator() {
        return new ForgeChunkGenerator(((IServerWorld) delegate).getLevel().getChunkSource().getGenerator());
    }

    @Override
    public String getName() {
        return ((IServerWorld) delegate).getLevel().toString();
    }

    @Override
    public Chunk getChunkAt(int x, int z) {
        return null;
    }

    @Override
    public Block getBlockAt(int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        return new ForgeBlock(pos, delegate);
    }

    @Override
    public Entity spawnEntity(Location location, EntityType entityType) {
        net.minecraft.entity.Entity entity = ForgeAdapter.adapt(entityType).create(((IServerWorld) delegate).getLevel());
        entity.setPos(location.getX(), location.getY(), location.getZ());
        delegate.addFreshEntity(entity);
        return new ForgeEntity(entity);
    }

    @Override
    public int getMinHeight() {
        return 0;
    }

    @Override
    public IWorld getHandle() {
        return delegate;
    }

    @Override
    public IWorld getWorld() {
        return delegate;
    }

    @Override
    public int hashCode() {
        return ((IServerWorld) delegate).getLevel().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ForgeWorldAccess)) return false;
        return ((IServerWorld) ((ForgeWorldAccess) obj).delegate).getLevel().equals(((IServerWorld) delegate).getLevel());
    }
}
