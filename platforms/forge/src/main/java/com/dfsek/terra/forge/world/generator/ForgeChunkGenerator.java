package com.dfsek.terra.forge.world.generator;

import com.dfsek.terra.api.platform.world.generator.ChunkGenerator;

public class ForgeChunkGenerator implements ChunkGenerator {
    private final net.minecraft.world.gen.ChunkGenerator delegate;

    public ForgeChunkGenerator(net.minecraft.world.gen.ChunkGenerator delegate) {
        this.delegate = delegate;
    }

    @Override
    public net.minecraft.world.gen.ChunkGenerator getHandle() {
        return delegate;
    }
}
