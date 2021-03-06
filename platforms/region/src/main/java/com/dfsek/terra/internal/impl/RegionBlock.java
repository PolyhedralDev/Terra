package com.dfsek.terra.internal.impl;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.BlockFace;
import com.dfsek.terra.api.platform.block.BlockType;
import com.dfsek.terra.api.platform.block.state.BlockState;

public class RegionBlock implements Block {
    private final RegionWorld world;
    private final RegionChunk chunk;
    private final Vector3 pos;
    private RegionData data = null;
    private RegionBlockState state = null;

    public RegionBlock(RegionWorld world, RegionChunk chunk, Vector3 pos) {
        this.world = world;
        this.chunk = chunk;
        this.pos = pos;
    }

    @Override
    public Object getHandle() {
        return data;
    }

    @Override
    public void setBlockData(BlockData data, boolean physics) {
        this.data = (RegionData) data;
    }

    @Override
    public BlockData getBlockData() {
        return data == null ? data = chunk.getBlockData(pos.getBlockX() % 16, pos.getBlockY(), pos.getBlockZ() % 16) : data;
    }

    @Override
    public BlockState getState() {
        return state == null ? state = new RegionBlockState(this) : state;
    }

    @Override
    public Block getRelative(BlockFace face) {
        return world.getBlockAt(pos.getBlockX() + face.getModX(), pos.getBlockY() + face.getModY(), pos.getBlockZ() + face.getModZ());
    }

    @Override
    public Block getRelative(BlockFace face, int len) {
        return world.getBlockAt(pos.getBlockX() + face.getModX() * len, pos.getBlockY() + face.getModY() * len, pos.getBlockZ() + face.getModZ() * len);
    }

    @Override
    public boolean isEmpty() {
        return getBlockData().isAir();
    }

    @Override
    public Location getLocation() {
        return pos.toLocation(world);
    }

    @Override
    public BlockType getType() {
        return getBlockData().getBlockType();
    }

    @Override
    public int getX() {
        return pos.getBlockX();
    }

    @Override
    public int getZ() {
        return pos.getBlockZ();
    }

    @Override
    public int getY() {
        return pos.getBlockY();
    }

    @Override
    public boolean isPassable() {
        return false;
    }
}
