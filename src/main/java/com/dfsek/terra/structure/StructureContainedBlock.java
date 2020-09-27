package com.dfsek.terra.structure;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.event.block.BlockDamageEvent;

import java.io.Serializable;

public class StructureContainedBlock implements Serializable {
    public static final long serialVersionUID = 6143969483382710947L;
    private final transient BlockData bl;
    private final String dataString;
    private final int x;
    private final int y;
    private final int z;
    public StructureContainedBlock(int x, int y, int z, BlockData block) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.bl = block;
        dataString = bl.getAsString(false);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public BlockData getBlockData() {
        return bl == null ? Bukkit.createBlockData(dataString) : bl;
    }

    public String getDataAsString() {
        return dataString;
    }
}
