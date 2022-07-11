package com.dfsek.terra.addons.chunkgenerator.layer.palette;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.chunk.generation.util.Palette;


public class BlockLayerPalette implements LayerPalette {
    
    private final Palette palette;
    
    public BlockLayerPalette(BlockState block) {
        this.palette = new SingletonPalette(block);
    }
    
    @Override
    public Palette get(long seed, Biome biome, int x, int y, int z) {
        return palette;
    }
    
    private record SingletonPalette(BlockState blockState) implements Palette {
        @Override
            public BlockState get(int layer, double x, double y, double z, long seed) {
                return blockState;
            }
        }
}
