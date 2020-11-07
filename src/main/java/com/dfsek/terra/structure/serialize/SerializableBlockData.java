package com.dfsek.terra.structure.serialize;

import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;

import java.io.Serializable;

public class SerializableBlockData implements Serializable {
    private static final long serialVersionUID = 5298928608478640008L;
    private final String data;

    public SerializableBlockData(BlockData d) {
        this.data = d.getAsString(false);
    }

    public BlockData getData() {
        return Bukkit.createBlockData(data);
    }
}
