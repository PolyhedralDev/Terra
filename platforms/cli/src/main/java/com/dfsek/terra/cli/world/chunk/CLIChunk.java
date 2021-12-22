package com.dfsek.terra.cli.world.chunk;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.api.world.chunk.Chunk;

import com.dfsek.terra.cli.block.CLIBlockState;

import com.dfsek.terra.cli.world.CLIWorld;

import org.jetbrains.annotations.NotNull;


public class CLIChunk implements Chunk {
    private final int x;
    private final int z;
    private final CLIBlockState[][][] blocks;
    private final int minHeight;
    private final CLIWorld world;
    
    public CLIChunk(int x, int z, CLIWorld world) {
        this.x = x;
        this.z = z;
        this.minHeight = world.getMinHeight();
        this.world = world;
        this.blocks= new CLIBlockState[16][16][world.getMaxHeight() - minHeight];
    }
    
    @Override
    public Object getHandle() {
        return null;
    }
    
    @Override
    public void setBlock(int x, int y, int z, BlockState data, boolean physics) {
        blocks[x][z][y - minHeight] = (CLIBlockState) data;
    }
    
    @Override
    public @NotNull BlockState getBlock(int x, int y, int z) {
        return blocks[x][z][y - minHeight];
    }
    
    @Override
    public int getX() {
        return x;
    }
    
    @Override
    public int getZ() {
        return z;
    }
    
    @Override
    public ServerWorld getWorld() {
        return world;
    }
}
