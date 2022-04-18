package com.dfsek.terra.cli.world.chunk;

import org.jetbrains.annotations.NotNull;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.api.world.chunk.Chunk;
import com.dfsek.terra.api.world.chunk.generation.ProtoChunk;
import com.dfsek.terra.cli.NBTSerializable;
import com.dfsek.terra.cli.block.CLIBlockState;
import com.dfsek.terra.cli.world.CLIWorld;

import static com.dfsek.terra.cli.handle.CLIWorldHandle.getAIR;


public class CLIChunk implements Chunk, ProtoChunk, NBTSerializable<net.querz.mca.Chunk> {
    private final int x;
    private final int z;
    private final CLIBlockState[][][] blocks;
    private final int minHeight;
    private final int maxHeight;
    private final CLIWorld world;
    
    public CLIChunk(int x, int z, CLIWorld world) {
        this.x = x;
        this.z = z;
        this.minHeight = world.getMinHeight();
        this.maxHeight = world.getMaxHeight();
        this.world = world;
        this.blocks = new CLIBlockState[16][16][maxHeight - minHeight];
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
    public @NotNull CLIBlockState getBlock(int x, int y, int z) {
        CLIBlockState blockState = blocks[x][z][y - minHeight];
        if(blockState == null) return getAIR();
        return blockState;
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
    
    @Override
    public net.querz.mca.Chunk serialize() {
        net.querz.mca.Chunk chunk = net.querz.mca.Chunk.newChunk(2230);
        for(int x = 0; x < blocks.length; x++) {
            for(int z = 0; z < blocks[x].length; z++) {
                for(int y = 0; y < blocks[z][z].length; y++) {
                    int yi = y + minHeight;
                    if(yi < 0 || yi >= 256) continue;
                    CLIBlockState blockState = blocks[x][z][y];
                    if(blockState != null) {
                        chunk.setBlockStateAt(x, yi, z, blockState.getNbt(), false);
                    }
                }
            }
        }
        chunk.setStatus("features");
        return chunk;
    }
    
    @Override
    public int getMaxHeight() {
        return maxHeight;
    }
}
