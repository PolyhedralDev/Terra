package com.dfsek.terra.minestom.chunk;


import com.dfsek.terra.api.block.state.BlockState;

import com.dfsek.terra.api.world.chunk.generation.ProtoChunk;
import com.dfsek.terra.minestom.block.MinestomBlockState;

import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.generator.UnitModifier;
import org.jetbrains.annotations.NotNull;


public class CachedChunk implements ProtoChunk {
    private final int minHeight;
    private final int maxHeight;
    private final Block[][][] blocks;

    public CachedChunk(int minHeight, int maxHeight) {
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.blocks = new Block[16][maxHeight - minHeight + 1][16];

        for(int x = 0; x < 16; x++) {
            for(int z = 0; z < 16; z++) {
                for(int y = 0; y < maxHeight - minHeight + 1; y++) {
                    blocks[x][y][z] = Block.AIR;
                }
            }
        }
    }

    public void writeRelative(UnitModifier modifier) {
        modifier.setAllRelative((x, y, z) -> blocks[x][y][z]);
    }

    @Override
    public void setBlock(int x, int y, int z, @NotNull BlockState blockState) {
        Block block = (Block) blockState.getHandle();
        if(block == null) return;
        blocks[x][y - minHeight][z] = block;
    }

    @Override
    public @NotNull BlockState getBlock(int x, int y, int z) {
        return new MinestomBlockState(blocks[x][y - minHeight][z]);
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
