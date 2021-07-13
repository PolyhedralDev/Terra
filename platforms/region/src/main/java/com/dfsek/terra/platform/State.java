package com.dfsek.terra.platform;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockState;
import net.querz.nbt.tag.CompoundTag;

public class State implements BlockState, BlockType {
    private final CompoundTag data;
    private final String noProp;

    public State(String data) {
        this.data = new CompoundTag();
        if(data.contains("[")) {
            noProp = data.substring(0, data.indexOf('[')); // Strip properties
            String properties = data.substring(data.indexOf('[') + 1, data.indexOf(']'));
            String[] props = properties.split(",");
            CompoundTag pTag = new CompoundTag();
            for(String property : props) {
                String name = property.substring(0, property.indexOf('='));
                String val = property.substring(property.indexOf('=') + 1);

                pTag.putString(name, val);
            }
            this.data.put("Properties", pTag);
        } else noProp = data;
        this.data.putString("Name", noProp);
    }

    public State(CompoundTag tag) {
        if(tag == null) {
            this.data = new CompoundTag();
            data.putString("Name", "minecraft:air");
        } else {
            this.data = tag;
        }
        noProp = data.getString("Name");
    }


    @Override
    public BlockType getBlockType() {
        return this;
    }

    @Override
    public boolean matches(BlockState other) {
        return ((State) other).noProp.equals(noProp);
    }

    @Override
    public boolean isAir() {
        return noProp.equals("minecraft:air");
    }

    @Override
    public boolean isStructureVoid() {
        return false;
    }


    @Override
    public BlockState clone() {
        try {
            return (BlockState) super.clone();
        } catch(CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    @Override
    public String getAsString() {
        return noProp;
    }

    @Override
    public CompoundTag getHandle() {
        return data;
    }

    @Override
    public int hashCode() {
        return noProp.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof State)) return false;
        return ((State) obj).noProp.equals(noProp);
    }

    @Override
    public BlockState getDefaultData() {
        return this;
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public boolean isWater() {
        return false;
    }
}
