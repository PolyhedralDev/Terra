package com.dfsek.terra.fabric.world.block;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.BlockType;
import com.dfsek.terra.fabric.mixin.access.StateAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.Registry;

import java.util.stream.Collectors;

public class FabricBlockData implements BlockData {
    protected BlockState delegate;

    public FabricBlockData(BlockState delegate) {
        this.delegate = delegate;
    }

    @Override
    public BlockType getBlockType() {
        return (BlockType) delegate.getBlock();
    }

    @Override
    public boolean matches(BlockData other) {
        return delegate.getBlock() == ((FabricBlockData) other).delegate.getBlock();
    }

    @Override
    public BlockData clone() {
        try {
            return (FabricBlockData) super.clone();
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
    public BlockState getHandle() {
        return delegate;
    }
}
