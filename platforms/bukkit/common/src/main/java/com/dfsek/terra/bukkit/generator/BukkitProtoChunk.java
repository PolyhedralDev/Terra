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

package com.dfsek.terra.bukkit.generator;

import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.world.chunk.generation.ProtoChunk;
import com.dfsek.terra.bukkit.world.block.data.BukkitBlockState;


public class BukkitProtoChunk implements ProtoChunk {
    
    private final ChunkGenerator.ChunkData delegate;
    
    public BukkitProtoChunk(ChunkGenerator.ChunkData delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public ChunkGenerator.ChunkData getHandle() {
        return delegate;
    }
    
    @Override
    public int getMaxHeight() {
        return delegate.getMaxHeight();
    }
    
    
    @Override
    public void setBlock(int x, int y, int z, @NotNull BlockState blockState) {
        delegate.setBlock(x, y, z, ((BukkitBlockState) blockState).getHandle());
    }
    
    
    @Override
    public @NotNull BlockState getBlock(int x, int y, int z) {
        return BukkitBlockState.newInstance(delegate.getBlockData(x, y, z));
    }
}
