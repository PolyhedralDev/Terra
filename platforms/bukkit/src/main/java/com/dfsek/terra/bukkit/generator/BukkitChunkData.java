package com.dfsek.terra.bukkit.generator;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.world.generator.ChunkData;
import com.dfsek.terra.bukkit.world.block.data.BukkitBlockState;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

public class BukkitChunkData implements ChunkData {

    private final ChunkGenerator.ChunkData delegate;

    public BukkitChunkData(ChunkGenerator.ChunkData delegate) {
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
