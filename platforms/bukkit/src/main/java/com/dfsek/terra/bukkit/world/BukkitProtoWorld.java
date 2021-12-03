package com.dfsek.terra.bukkit.world;

import org.bukkit.Location;
import org.bukkit.generator.LimitedRegion;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.api.world.chunk.generation.ProtoWorld;
import com.dfsek.terra.bukkit.BukkitEntity;
import com.dfsek.terra.bukkit.generator.BukkitChunkGeneratorWrapper;
import com.dfsek.terra.bukkit.world.block.data.BukkitBlockState;
import com.dfsek.terra.bukkit.world.block.state.BukkitBlockEntity;
import com.dfsek.terra.bukkit.world.entity.BukkitEntityType;


public class BukkitProtoWorld implements ProtoWorld {
    private final LimitedRegion delegate;
    
    public BukkitProtoWorld(LimitedRegion delegate) { this.delegate = delegate; }
    
    @Override
    public LimitedRegion getHandle() {
        return delegate;
    }
    
    @Override
    public void setBlockData(int x, int y, int z, BlockState data, boolean physics) {
        delegate.setBlockData(x, y, z, BukkitAdapter.adapt(data));
        if(physics) {
            delegate.scheduleBlockUpdate(x, y, z);
        }
    }
    
    @Override
    public long getSeed() {
        return delegate.getWorld().getSeed();
    }
    
    @Override
    public int getMaxHeight() {
        return delegate.getWorld().getMaxHeight();
    }
    
    @Override
    public BlockState getBlockData(int x, int y, int z) {
        return BukkitBlockState.newInstance(delegate.getBlockData(x, y, z));
    }
    
    @Override
    public BlockEntity getBlockState(int x, int y, int z) {
        return BukkitBlockEntity.newInstance(delegate.getBlockState(x, y, z));
    }
    
    @Override
    public int getMinHeight() {
        return delegate.getWorld().getMinHeight();
    }
    
    @Override
    public Entity spawnEntity(double x, double y, double z, EntityType entityType) {
        return new BukkitEntity(
                delegate.spawnEntity(new Location(delegate.getWorld(), x, y, z), ((BukkitEntityType) entityType).getHandle()));
    
    }
    
    @Override
    public ChunkGenerator getGenerator() {
        return ((BukkitChunkGeneratorWrapper) delegate.getWorld().getGenerator()).getHandle();
    }
    
    @Override
    public BiomeProvider getBiomeProvider() {
        return ((BukkitChunkGeneratorWrapper) delegate.getWorld().getGenerator()).getPack().getBiomeProvider();
    }
    
    @Override
    public int centerChunkX() {
        return delegate.getCenterChunkX();
    }
    
    @Override
    public int centerChunkZ() {
        return delegate.getCenterChunkZ();
    }
    
    @Override
    public ServerWorld getWorld() {
        return new BukkitServerWorld(delegate.getWorld());
    }
}
