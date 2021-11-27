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

import java.io.File;
import java.util.UUID;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.WorldConfig;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.generator.ChunkGenerator;
import com.dfsek.terra.bukkit.BukkitEntity;
import com.dfsek.terra.bukkit.generator.BukkitChunkGeneratorWrapper;
import com.dfsek.terra.bukkit.world.block.state.BukkitBlockEntity;
import com.dfsek.terra.bukkit.world.entity.BukkitEntityType;


public class BukkitWorld implements World {
    private final org.bukkit.World delegate;
    
    public BukkitWorld(org.bukkit.World delegate) {
        this.delegate = delegate;
    }
    
    public String getName() {
        return delegate.getName();
    }
    
    public UUID getUID() {
        return delegate.getUID();
    }
    
    public boolean isChunkGenerated(int x, int z) {
        return delegate.isChunkGenerated(x, z);
    }
    
    public File getWorldFolder() {
        return delegate.getWorldFolder();
    }
    
    @Override
    public Entity spawnEntity(Vector3 location, EntityType entityType) {
        return new BukkitEntity(
                delegate.spawnEntity(BukkitAdapter.adapt(location).toLocation(delegate), ((BukkitEntityType) entityType).getHandle()));
    }
    
    @Override
    public void setBlockData(int x, int y, int z, BlockState data, boolean physics) {
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
    public BlockState getBlockData(int x, int y, int z) {
        return BukkitAdapter.adapt(delegate.getBlockAt(x, y, z).getBlockData());
    }
    
    @Override
    public BlockEntity getBlockState(int x, int y, int z) {
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
        return ((BukkitChunkGeneratorWrapper) delegate.getGenerator()).getWorldConfig().getProvider();
    }
    
    @Override
    public WorldConfig getConfig() {
        return ((BukkitChunkGeneratorWrapper) delegate.getGenerator()).getWorldConfig();
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
        if(!(obj instanceof BukkitWorld other)) return false;
        return other.getHandle().equals(delegate);
    }
    
    @Override
    public String toString() {
        return delegate.toString();
    }
}
