package com.dfsek.terra.config.dummy;

import com.dfsek.terra.api.block.BlockData;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.generator.ChunkGenerator;
import com.dfsek.terra.api.world.generator.GeneratorWrapper;

public class DummyWorld implements World {
    @Override
    public Object getHandle() {
        throw new UnsupportedOperationException("Cannot get handle of DummyWorld");
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
    public ChunkGenerator getGenerator() {
        return () -> (GeneratorWrapper) () -> null;
    }

    @Override
    public Chunk getChunkAt(int x, int z) {
        throw new UnsupportedOperationException("Cannot get chunk in DummyWorld");
    }

    @Override
    public BlockData getBlockData(int x, int y, int z) {
        throw new UnsupportedOperationException("Cannot get block in DummyWorld");
    }

    @Override
    public void setBlockData(int x, int y, int z, BlockData data, boolean physics) {
        throw new UnsupportedOperationException("Cannot set block in DummyWorld");
    }

    @Override
    public BlockState getBlockState(int x, int y, int z) {
        throw new UnsupportedOperationException("Cannot get block in DummyWorld");
    }

    @Override
    public Entity spawnEntity(Vector3 location, EntityType entityType) {
        throw new UnsupportedOperationException("Cannot spawn entity in DummyWorld");
    }

    @Override
    public int getMinHeight() {
        return 0;
    }
}
