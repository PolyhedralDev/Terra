package com.dfsek.terra.fabric.world.generator;

import com.dfsek.terra.api.platform.world.generator.ChunkGenerator;

public class FabricChunkGenerator implements ChunkGenerator {
    private final net.minecraft.world.gen.chunk.ChunkGenerator delegate;

    public FabricChunkGenerator(net.minecraft.world.gen.chunk.ChunkGenerator delegate) {
        this.delegate = delegate;
    }

    @Override
    public net.minecraft.world.gen.chunk.ChunkGenerator getHandle() {
        return delegate;
    }
}
