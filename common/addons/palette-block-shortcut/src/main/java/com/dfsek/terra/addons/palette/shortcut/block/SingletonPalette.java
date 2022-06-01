package com.dfsek.terra.addons.palette.shortcut.block;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.world.chunk.generation.util.Palette;


public class SingletonPalette implements Palette {
    private final BlockState blockState;
    
    public SingletonPalette(BlockState blockState) {
        this.blockState = blockState;
    }
    
    @Override
    public BlockState get(int layer, double x, double y, double z, long seed) {
        return blockState;
    }
}
