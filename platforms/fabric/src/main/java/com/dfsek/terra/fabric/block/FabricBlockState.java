package com.dfsek.terra.fabric.block;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.fabric.mixin.access.StateAccessor;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.Registry;

import java.util.stream.Collectors;

public class FabricBlockState implements BlockState {
    protected net.minecraft.block.BlockState delegate;

    public FabricBlockState(net.minecraft.block.BlockState delegate) {
        this.delegate = delegate;
    }

    @Override
    public BlockType getBlockType() {
        return (BlockType) delegate.getBlock();
    }

    @Override
    public boolean matches(BlockState other) {
        return delegate.getBlock() == ((FabricBlockState) other).delegate.getBlock();
    }

    @Override
    public BlockState clone() {
        try {
            return (FabricBlockState) super.clone();
        } catch(CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    @Override
    public String getAsString() {
        StringBuilder data = new StringBuilder(Registry.BLOCK.getId(delegate.getBlock()).toString());
        if(!delegate.getEntries().isEmpty()) {
            data.append('[');
            data.append(delegate.getEntries().entrySet().stream().map(StateAccessor.getPropertyMapPrinter()).collect(Collectors.joining(",")));
            data.append(']');
        }
        return data.toString();
    }

    @Override
    public boolean isAir() {
        return delegate.isAir();
    }

    @Override
    public boolean isStructureVoid() {
        return delegate.getBlock() == Blocks.STRUCTURE_VOID;
    }

    @Override
    public net.minecraft.block.BlockState getHandle() {
        return delegate;
    }
}
