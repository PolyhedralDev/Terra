package com.dfsek.terra.internal.impl;

import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.internal.InternalChunk;
import com.dfsek.terra.internal.InternalWorld;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class RegionChunk implements InternalChunk {
    //    private final RegionData[] regionData = new RegionData[16 * 16 * 255];
    private final RegionData[][][] regionData = new RegionData[16][16][256];
    private final RegionWorld world;
    private final int x;
    private final int z;

    public RegionChunk(RegionWorld world, int x, int z) {
        this.world = world;
        this.x = x;
        this.z = z;
    }


    @Override
    public Object getHandle() {
        return null;
    }

    @Override
    public int getMaxHeight() {
        return 255;
    }

    @Override
    public void setBlock(int x, int y, int z, @NotNull BlockData blockData) {
        if(x >= 0 && x < 16 && y >= 0 && y < getMaxHeight() && z >= 0 && z < 16) {
            regionData[x][z][y] = (RegionData) blockData;
        }
    }

    @Override
    public @NotNull RegionData getBlockData(int x, int y, int z) {
        if(x >= 0 && x < 16 && y >= 0 && y < getMaxHeight() && z >= 0 && z < 16)
            return regionData[x][z][y];
        else
            return null; // haha
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        return new RegionBlock(world, this, new Vector3(x, y, z));
    }

    @Override
    public InternalWorld getWorld() {
        return world;
    }

    @Override
    public String toString() {
        return "RegionChunk{" +
                "regionData=" + Arrays.deepToString(regionData) +
                ", x=" + x +
                ", z=" + z +
                '}';
    }
}
