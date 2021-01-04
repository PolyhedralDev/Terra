package com.dfsek.terra.platform;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.MaterialData;
import net.querz.nbt.tag.CompoundTag;

public class Data implements BlockData, MaterialData {
    private final CompoundTag data;
    private final String noProp;

    public Data(String data) {
        this.data = new CompoundTag();
        if(data.contains("[")) noProp = data.substring(0, data.indexOf('[')); // Strip properties for now TODO: actually do properties lol
        else noProp = data;
        this.data.putString("Name", noProp);
    }

    @Override
    public MaterialData getMaterial() {
        return this;
    }

    @Override
    public boolean matches(MaterialData materialData) {
        return ((Data) materialData).noProp.equals(noProp);
    }

    @Override
    public boolean matches(BlockData other) {
        return ((Data) other).noProp.equals(noProp);
    }

    @Override
    public boolean isSolid() {
        return !isAir(); //TODO: actual implementation
    }

    @Override
    public boolean isAir() {
        return noProp.equals("minecraft:air");
    }

    @Override
    public double getMaxDurability() {
        return 0;
    }

    @Override
    public BlockData createBlockData() {
        return this;
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
}
