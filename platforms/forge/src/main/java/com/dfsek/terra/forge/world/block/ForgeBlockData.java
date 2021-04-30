package com.dfsek.terra.forge.world.block;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.BlockType;
import com.dfsek.terra.forge.world.ForgeAdapter;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

public class ForgeBlockData implements BlockData {
    protected BlockState delegate;

    public ForgeBlockData(BlockState delegate) {
        this.delegate = delegate;
    }

    @Override
    public BlockType getBlockType() {
        return ForgeAdapter.adapt(delegate.getBlock());
    }

    @Override
    public boolean matches(BlockData other) {
        return delegate.getBlock() == ((ForgeBlockData) other).delegate.getBlock();
    }

    @Override
    public BlockData clone() {
        try {
            return (ForgeBlockData) super.clone();
        } catch(CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    @Override
    public String getAsString() {

        /*
        StringBuilder data = new StringBuilder(Registry.BLOCK.getId(delegate.getBlock()).toString());
        if(!delegate.getProperties().isEmpty()) {
            data.append('[');
            data.append(delegate.getProperties().stream().map(State.PROPERTY_MAP_PRINTER).collect(Collectors.joining(",")));
            data.append(']');
        }
        return data.toString();

         */
        throw new UnsupportedOperationException("TODO: implement this"); // TODO
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
