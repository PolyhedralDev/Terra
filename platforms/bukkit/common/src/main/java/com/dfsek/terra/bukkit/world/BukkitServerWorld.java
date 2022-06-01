/*
 * This file is part of Terra.
 *
 * Terra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Terra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Terra.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.dfsek.terra.bukkit.world;

import org.bukkit.Location;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.Chunk;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.bukkit.BukkitEntity;
import com.dfsek.terra.bukkit.generator.BukkitChunkGeneratorWrapper;
import com.dfsek.terra.bukkit.world.block.state.BukkitBlockEntity;
import com.dfsek.terra.bukkit.world.entity.BukkitEntityType;


public class BukkitServerWorld implements ServerWorld {
    private final org.bukkit.World delegate;
    
    public BukkitServerWorld(org.bukkit.World delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public Entity spawnEntity(double x, double y, double z, EntityType entityType) {
        return new BukkitEntity(
                delegate.spawnEntity(new Location(delegate, x, y, z), ((BukkitEntityType) entityType).getHandle()));
    }
    
    @Override
    public void setBlockState(int x, int y, int z, BlockState data, boolean physics) {
        delegate.getBlockAt(x, y, z).setBlockData(BukkitAdapter.adapt(data), physics);
    }
    
    @Override
    public long getSeed() {
        return delegate.getSeed();
    }
    
    @Override
    public int getMaxHeight() {
        return delegate.getMaxHeight();
    }
    
    @Override
    public Chunk getChunkAt(int x, int z) {
        return BukkitAdapter.adapt(delegate.getChunkAt(x, z));
    }
    
    @Override
    public BlockState getBlockState(int x, int y, int z) {
        return BukkitAdapter.adapt(delegate.getBlockAt(x, y, z).getBlockData());
    }
    
    @Override
    public BlockEntity getBlockEntity(int x, int y, int z) {
        return BukkitBlockEntity.newInstance(delegate.getBlockAt(x, y, z).getState());
    }
    
    @Override
    public int getMinHeight() {
        return delegate.getMinHeight();
    }
    
    @Override
    public ChunkGenerator getGenerator() {
        return ((BukkitChunkGeneratorWrapper) delegate.getGenerator()).getHandle();
    }
    
    @Override
    public BiomeProvider getBiomeProvider() {
        return ((BukkitChunkGeneratorWrapper) delegate.getGenerator()).getPack().getBiomeProvider();
    }
    
    @Override
    public ConfigPack getPack() {
        return ((BukkitChunkGeneratorWrapper) delegate.getGenerator()).getPack();
    }
    
    @Override
    public org.bukkit.World getHandle() {
        return delegate;
    }
    
    @Override
    public int hashCode() {
        return delegate.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof BukkitServerWorld other)) return false;
        return other.getHandle().equals(delegate);
    }
    
    @Override
    public String toString() {
        return delegate.toString();
    }
}
