package com.dfsek.terra.forge.world.generator;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.world.generator.ChunkData;
import com.dfsek.terra.forge.world.block.ForgeBlockData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunk;
import org.jetbrains.annotations.NotNull;

public class ForgeChunkData implements ChunkData {
    private final IChunk handle;

    public ForgeChunkData(IChunk handle) {
        this.handle = handle;
    }

    @Override
    public IChunk getHandle() {
        return handle;
    }

    @Override
    public int getMaxHeight() {
        return handle.getMaxBuildHeight();
    }

    @Override
    public void setBlock(int x, int y, int z, @NotNull BlockData blockData) {
        handle.setBlockState(new BlockPos(x, y, z), ((ForgeBlockData) blockData).getHandle(), false);
    }

    @Override
    public @NotNull BlockData getBlockData(int x, int y, int z) {
        return new ForgeBlockData(handle.getBlockState(new BlockPos(x, y, z)));
    }
}
