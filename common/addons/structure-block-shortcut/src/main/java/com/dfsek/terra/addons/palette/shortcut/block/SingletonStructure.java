package com.dfsek.terra.addons.palette.shortcut.block;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.util.Rotation;
import com.dfsek.terra.api.util.vector.Vector3Int;
import com.dfsek.terra.api.world.WritableWorld;

import java.util.Random;


public class SingletonStructure implements Structure {
    private final BlockState blockState;
    
    public SingletonStructure(BlockState blockState) {
        this.blockState = blockState;
    }
    
    @Override
    public boolean generate(Vector3Int location, WritableWorld world, Random random, Rotation rotation) {
        world.setBlockState(location, blockState);
        return true;
    }
}
