package com.dfsek.terra.bukkit.generator;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.world.generator.ChunkData;
import com.dfsek.terra.bukkit.world.block.data.BukkitBlockState;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

public class BukkitChunkGenerator implements com.dfsek.terra.api.world.generator.ChunkGenerator {
    private final ChunkGenerator delegate;

    public BukkitChunkGenerator(ChunkGenerator delegate) {
        this.delegate = delegate;
    }

    @Override
    public ChunkGenerator getHandle() {
        return delegate;
    }

}
