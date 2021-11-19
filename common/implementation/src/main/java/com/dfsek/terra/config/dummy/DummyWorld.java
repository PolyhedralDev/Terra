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

package com.dfsek.terra.config.dummy;

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


public class DummyWorld implements World {
    @Override
    public Entity spawnEntity(Vector3 location, EntityType entityType) {
        throw new UnsupportedOperationException("Cannot spawn entity in DummyWorld");
    }
    
    @Override
    public void setBlockData(int x, int y, int z, BlockState data, boolean physics) {
        throw new UnsupportedOperationException("Cannot set block in DummyWorld");
    }
    
    @Override
    public long getSeed() {
        return 2403L;
    }
    
    @Override
    public int getMaxHeight() {
        return 255;
    }
    
    @Override
    public Chunk getChunkAt(int x, int z) {
        throw new UnsupportedOperationException("Cannot get chunk in DummyWorld");
    }
    
    @Override
    public BlockState getBlockData(int x, int y, int z) {
        throw new UnsupportedOperationException("Cannot get block in DummyWorld");
    }
    
    @Override
    public BlockEntity getBlockState(int x, int y, int z) {
        throw new UnsupportedOperationException("Cannot get block in DummyWorld");
    }
    
    @Override
    public int getMinHeight() {
        return 0;
    }
    
    @Override
    public ChunkGenerator getGenerator() {
        throw new UnsupportedOperationException("Cannot get generator of DummyWorld");
    }
    
    @Override
    public BiomeProvider getBiomeProvider() {
        throw new UnsupportedOperationException("Cannot get biome provider of DummyWorld");
    }
    
    @Override
    public WorldConfig getConfig() {
        throw new UnsupportedOperationException("Cannot get config of DummyWorld");
    }
    
    @Override
    public Object getHandle() {
        throw new UnsupportedOperationException("Cannot get handle of DummyWorld");
    }
}
