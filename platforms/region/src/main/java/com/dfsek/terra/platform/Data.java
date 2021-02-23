package com.dfsek.terra.platform;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.BlockType;
import net.querz.nbt.tag.CompoundTag;

public class Data implements BlockData, BlockType {
    private final CompoundTag data;
    private final String noProp;

    public Data(String data) {
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

    public Data(CompoundTag tag) {
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
    public boolean matches(BlockData other) {
        return ((Data) other).noProp.equals(noProp);
    }

    @Override
    public boolean isAir() {
        return noProp.equals("minecraft:air");
    }


    @Override
    public BlockData clone() {
        try {
            return (BlockData) super.clone();
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
        if(!(obj instanceof Data)) return false;
        return ((Data) obj).noProp.equals(noProp);
    }

    @Override
    public BlockData getDefaultData() {
        return this;
    }

    @Override
    public boolean isSolid() {
        return false;
    }
}
