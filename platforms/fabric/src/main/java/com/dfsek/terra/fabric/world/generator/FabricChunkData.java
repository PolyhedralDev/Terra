package com.dfsek.terra.fabric.world.generator;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.world.generator.ChunkData;
import com.dfsek.terra.fabric.world.block.FabricBlockData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import org.jetbrains.annotations.NotNull;

public class FabricChunkData implements ChunkData {
    private final Chunk handle;

    public FabricChunkData(Chunk handle) {
        this.handle = handle;
    }

    @Override
    public Chunk getHandle() {
        return handle;
    }

    @Override
    public int getMaxHeight() {
        return handle.getHeight();
    }

    @Override
    public void setBlock(int x, int y, int z, @NotNull BlockData blockData) {
        handle.setBlockState(new BlockPos(x, y, z), ((FabricBlockData) blockData).getHandle(), false);
    }

    @Override
    public @NotNull BlockData getBlockData(int x, int y, int z) {
        return new FabricBlockData(handle.getBlockState(new BlockPos(x, y, z)));
    }
}
