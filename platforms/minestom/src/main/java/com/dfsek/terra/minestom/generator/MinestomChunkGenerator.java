package com.dfsek.terra.minestom.generator;

import com.dfsek.terra.api.platform.world.generator.ChunkGenerator;

public class MinestomChunkGenerator implements ChunkGenerator {
    private final net.minestom.server.instance.ChunkGenerator delegate;

    public MinestomChunkGenerator(net.minestom.server.instance.ChunkGenerator delegate) {
        this.delegate = delegate;
    }

    @Override
    public net.minestom.server.instance.ChunkGenerator getHandle() {
        return delegate;
    }
}
