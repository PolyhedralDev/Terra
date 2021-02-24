package com.dfsek.terra.fabric.world.block;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.BlockType;
import com.dfsek.terra.fabric.world.FabricAdapter;
import net.minecraft.block.BlockState;
import net.minecraft.state.State;
import net.minecraft.util.registry.Registry;

import java.util.stream.Collectors;

public class FabricBlockData implements BlockData {
    protected BlockState delegate;

    public FabricBlockData(BlockState delegate) {
        this.delegate = delegate;
    }

    @Override
    public BlockType getBlockType() {
        return FabricAdapter.adapt(delegate.getBlock());
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
            data.append(delegate.getEntries().entrySet().stream().map(State.PROPERTY_MAP_PRINTER).collect(Collectors.joining(",")));
            data.append(']');
        }
        return data.toString();
    }

    @Override
    public boolean isAir() {
        return delegate.isAir();
    }

    @Override
    public BlockState getHandle() {
        return delegate;
    }
}
