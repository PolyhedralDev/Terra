package com.dfsek.terra.addons.sponge;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.structure.buffer.Buffer;
import com.dfsek.terra.api.structure.buffer.BufferedItem;
import com.dfsek.terra.api.structure.rotation.Rotation;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.World;

import java.util.Random;


public class SpongeStructure implements Structure {
    
    private final BufferedItem[][][] blocks;
    
    public SpongeStructure() {
        blocks = new BufferedItem[0][][];
    }
    
    @Override
    public boolean generate(Vector3 location, World world, Chunk chunk, Random random, Rotation rotation) {
        return true;
    }
    
    @Override
    public boolean generate(Buffer buffer, World world, Random random, Rotation rotation, int recursions) {
        return true;
    }
    
    @Override
    public boolean generate(Vector3 location, World world, Random random, Rotation rotation) {
        return true;
    }
    
    @Override
    public String getID() {
        return null;
    }
}
