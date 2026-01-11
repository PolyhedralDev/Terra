package com.dfsek.terra.minestom.block;

import net.minestom.server.instance.block.Block;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockState;


public class MinestomBlockType implements BlockType {
    private final Block block;

    public MinestomBlockType(Block block) {
        this.block = block;
    }

    @Override
    public BlockState defaultState() {
        return new MinestomBlockState(block);
    }

    @Override
    public boolean solid() {
        return block.isSolid();
    }

    @Override
    public boolean water() {
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
            return block.stateId() == other.block.stateId();
        }
        return false;
    }
}
