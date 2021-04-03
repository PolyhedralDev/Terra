package com.dfsek.terra.minestom.world;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.BlockType;
import net.minestom.server.instance.block.Block;

public class MinestomBlockData implements BlockData, BlockType {
    private final Block delegate;

    public MinestomBlockData(Block delegate) {
        if(delegate == null) throw new NullPointerException("Delegate must not be null");
        this.delegate = delegate;
    }

    @Override
    public Block getHandle() {
        return delegate;
    }

    @Override
    public BlockType getBlockType() {
        return this;
    }

    @Override
    public boolean matches(BlockData other) {
        return delegate == ((MinestomBlockData) other).delegate;
    }

    @Override
    public BlockData clone() {
        BlockData clone;
        try {
            clone = (BlockData) super.clone();
        } catch(CloneNotSupportedException e) {
            throw new Error(e);
        }
        return clone;
    }

    @Override
    public String getAsString() {
        return delegate.getName();
    }

    @Override
    public boolean isAir() {
        return delegate.isAir();
    }

    @Override
    public boolean isStructureVoid() {
        return delegate == Block.STRUCTURE_VOID;
    }

    @Override
    public BlockData getDefaultData() {
        return this;
    }

    @Override
    public boolean isSolid() {
        return delegate.isSolid();
    }

    @Override
    public boolean isWater() {
        return delegate == Block.WATER;
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof MinestomBlockData)) return false;
        return ((MinestomBlockData) obj).delegate == delegate;
    }
}
