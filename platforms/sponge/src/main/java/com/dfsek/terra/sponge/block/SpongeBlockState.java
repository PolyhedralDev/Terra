package com.dfsek.terra.sponge.block;

import org.spongepowered.api.block.BlockTypes;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.state.properties.Property;


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
    public boolean matches(BlockState other) {
        return delegate.type().equals(((SpongeBlockType) other.getBlockType()).getHandle());
    }
    
    @Override
    public <T> boolean has(Property<T> property) {
        return false;
    }
    
    @Override
    public <T> T get(Property<T> property) {
        return null;
    }
    
    @Override
    public <T> BlockState set(Property<T> property, T value) {
        return this;
    }
    
    @Override
    public BlockType getBlockType() {
        return new SpongeBlockType(delegate.type());
    }
    
    @Override
    public String getAsString() {
        return delegate.toString();
    }
    
    @Override
    public boolean isAir() {
        return delegate.type().equals(BlockTypes.AIR.get());
    }
    
    @Override
    public boolean isStructureVoid() {
        return delegate.type().equals(BlockTypes.STRUCTURE_VOID.get());
    }
    
    @Override
    public BlockState clone() {
        return this;
    }
}
