package com.dfsek.terra.cli.block;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.state.properties.Property;

import net.querz.nbt.tag.CompoundTag;


public class CLIBlockState implements BlockState {
    private final String value;
    private final CLIBlockType type;
    private final boolean isAir;
    private final CompoundTag nbt;
    
    public CLIBlockState(String value) {
        this.value = value;
        if(value.contains("[")) {
            this.type = new CLIBlockType(value.substring(0, value.indexOf("[")));
        } else {
            this.type = new CLIBlockType(value);
        }
        this.isAir = value.startsWith("minecraft:air");
        this.nbt = new CompoundTag();
        this.nbt.putString("Name", value);
    }
    
    @Override
    public Object getHandle() {
        return value;
    }
    
    @Override
    public boolean matches(BlockState other) {
        return false;
    }
    
    @Override
    public <T extends Comparable<T>> boolean has(Property<T> property) {
        return false;
    }
    
    @Override
    public <T extends Comparable<T>> T get(Property<T> property) {
        return null;
    }
    
    @Override
    public <T extends Comparable<T>> BlockState set(Property<T> property, T value) {
        return null;
    }
    
    @Override
    public BlockType getBlockType() {
        return type;
    }
    
    @Override
    public String getAsString(boolean properties) {
        return value;
    }
    
    @Override
    public boolean isAir() {
        return isAir;
    }
    
    public CompoundTag getNbt() {
        return nbt;
    }
}
