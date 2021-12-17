package com.dfsek.terra.addons.palette.shortcut.block;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.structure.buffer.Buffer;
import com.dfsek.terra.api.structure.buffer.items.BufferedBlock;
import com.dfsek.terra.api.util.Rotation;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.WritableWorld;
import com.dfsek.terra.api.world.chunk.Chunk;
import com.dfsek.terra.api.world.chunk.generation.util.Palette;

import java.util.Random;


public class SingletonStructure implements Structure {
    private final BlockState blockState;
    
    public SingletonStructure(BlockState blockState) {
        this.blockState = blockState;
    }
    
    @Override
    public boolean generate(Vector3 location, WritableWorld world, Chunk chunk, Random random, Rotation rotation) {
        world.setBlockState(location, blockState);
        return true;
    }
    
    @Override
    public boolean generate(Buffer buffer, WritableWorld world, Random random, Rotation rotation, int recursions) {
        world.setBlockState(buffer.getOrigin(), blockState);
        return true;
    }
    
    @Override
    public boolean generate(Vector3 location, WritableWorld world, Random random, Rotation rotation) {
        world.setBlockState(location, blockState);
        return true;
    }
    
    @Override
    public String getID() {
        return blockState.getAsString();
    }
}
