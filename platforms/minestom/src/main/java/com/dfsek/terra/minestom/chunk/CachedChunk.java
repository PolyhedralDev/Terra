package com.dfsek.terra.minestom.chunk;


import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.generator.UnitModifier;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.world.chunk.generation.ProtoChunk;
import com.dfsek.terra.minestom.block.MinestomBlockState;


public class CachedChunk implements ProtoChunk {
    private final int minHeight;
    private final int maxHeight;
    private final Block[] blocks;

    public CachedChunk(int minHeight, int maxHeight) {
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.blocks = new Block[16 * (maxHeight - minHeight + 1) * 16];
        Arrays.fill(blocks, Block.AIR);
    }

    public void writeRelative(UnitModifier modifier) {
        modifier.setAllRelative((x, y, z) -> blocks[getIndex(x, y + minHeight, z)]);
    }

    @Override
    public void setBlock(int x, int y, int z, @NotNull BlockState blockState) {
        Block block = (Block) blockState.getHandle();
        if(block == null) return;
        int index = getIndex(x, y, z);
        if (index > blocks.length || index < 0) return;
        blocks[index] = block;
    }

    private int getIndex(int x, int y, int z) {
        int y_normalized = y - minHeight;
        return x + (z * 16) + (y_normalized * 256);
    }

    @Override
    public @NotNull BlockState getBlock(int x, int y, int z) {
        int index = getIndex(x, y, z);
        if (index > blocks.length || index < 0) return MinestomBlockState.AIR;
        return new MinestomBlockState(blocks[index]);
    }

    @Override
    public Object getHandle() {
        return blocks;
    }

    @Override
    public int getMaxHeight() {
        return maxHeight;
    }
}
