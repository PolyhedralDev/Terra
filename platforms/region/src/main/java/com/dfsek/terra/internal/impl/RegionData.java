package com.dfsek.terra.internal.impl;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.BlockType;
import com.dfsek.terra.internal.InternalData;
import com.dfsek.terra.platform.Data;

import java.util.Objects;

public class RegionData implements InternalData {
    private final String data;

    public RegionData(String data) {
        this.data = data;
    }

    @Override
    public BlockType getBlockType() {
        return this;
    }

    @Override
    public boolean matches(BlockData other) {
        return ((RegionData) other).data.equals(data);
    }

    @Override
    public String getAsString() {
        return data;
    }

    @Override
    public boolean isAir() {
        return data.contains("minecraft:air");
    }

    @Override
    public BlockData getDefaultData() {
        return null;
    }

    @Override
    public boolean isSolid() {
        return !isAir(); //TODO: actual implementation
    }

    @Override
    public Object getHandle() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o == null || o.getClass() == Data.class) {
            return false;
        }
        RegionData that = (RegionData) o;
        return Objects.equals(data, that.data);
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
    public String toString() {
        return "RegionData{" +
                "data='" + data + '\'' +
                '}';
    }
}
