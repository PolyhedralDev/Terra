package com.dfsek.terra.minestom.block;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockState;

import net.minestom.server.instance.block.Block;


public class MinestomBlockType implements BlockType {
    private final Block block;

    public MinestomBlockType(Block block) {
        this.block = block;
    }

    @Override
    public BlockState getDefaultState() {
        return new MinestomBlockState(block);
    }

    @Override
    public boolean isSolid() {
        return block.isSolid();
    }

    @Override
    public boolean isWater() {
        return block.isLiquid();
    }

    @Override
    public Object getHandle() {
        return block;
    }

    @Override
    public int hashCode() {
        return block.id();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MinestomBlockType other) {
            return block.id() == other.block.id();
        }
        return false;
    }
}
