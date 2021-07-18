package com.dfsek.terra.sponge.world.block.data;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockState;

public class SpongeBlockState implements BlockState {
    private final org.spongepowered.api.block.BlockState delegate;

    public SpongeBlockState(org.spongepowered.api.block.BlockState delegate) {
        this.delegate = delegate;
    }

    @Override
    public org.spongepowered.api.block.BlockState getHandle() {
        return delegate;
    }

    @Override
    public BlockType getBlockType() {
        return null;
    }

    @Override
    public boolean matches(BlockState other) {
        return false;
    }

    @Override
    public BlockState clone() {
        return null;
    }

    @Override
    public String getAsString() {
        return null;
    }

    @Override
    public boolean isAir() {
        return false;
    }

    @Override
    public boolean isStructureVoid() {
        return false;
    }
}
