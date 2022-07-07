package com.dfsek.terra.cli.block;

import net.querz.nbt.tag.CompoundTag;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.state.properties.Property;


public class CLIBlockState implements BlockState {
    private final String value;
    private final CLIBlockType type;
    private final boolean isAir;
    private final CompoundTag nbt;
    
    public CLIBlockState(String value) {
        this.value = value;
        if(value.contains("[")) {

        } else {

        }
        this.isAir = value.startsWith("minecraft:air");
        this.nbt = new CompoundTag();
        if(value.contains("[")) {
            this.type = new CLIBlockType(value.substring(0, value.indexOf("[")));
            String properties = value.substring(value.indexOf('[') + 1, value.indexOf(']'));
            String[] props = properties.split(",");
            CompoundTag pTag = new CompoundTag();
            for(String property : props) {
                String name = property.substring(0, property.indexOf('='));
                String val = property.substring(property.indexOf('=') + 1);
                
                pTag.putString(name, val);
            }
            this.nbt.put("Properties", pTag);
        } else this.type = new CLIBlockType(value);
        this.nbt.putString("Name", type.getHandle());
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
