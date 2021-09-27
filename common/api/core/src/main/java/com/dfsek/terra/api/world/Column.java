package com.dfsek.terra.api.world;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.structure.feature.BinaryColumn;

import java.util.function.IntConsumer;


/**
 * A single vertical column of a world.
 */
public interface Column {
    int getX();
    
    int getZ();
    
    BlockState getBlock(int y);
    
    World getWorld();
    
    int getMinY();
    
    int getMaxY();
    
    void forEach(IntConsumer function);
    
    BinaryColumn newBinaryColumn();
}
