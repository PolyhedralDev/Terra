package com.dfsek.terra.platform;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.BlockFace;
import com.dfsek.terra.api.platform.block.MaterialData;
import com.dfsek.terra.api.platform.block.state.BlockState;
import net.jafama.FastMath;

public class DirectBlock implements Block {
    private final DirectWorld world;
    private final Vector3 pos;

    public DirectBlock(DirectWorld world, Vector3 pos) {
        this.world = world;
        this.pos = pos;
    }

    @Override
    public void setBlockData(BlockData data, boolean physics) {
        synchronized(world) {
            world.compute(FastMath.floorDiv(pos.getBlockX(), 16), FastMath.floorDiv(pos.getBlockZ(), 16)).setBlockStateAt(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ(), ((Data) data).getHandle(), false);
        }
    }

    @Override
    public BlockData getBlockData() {
        return new Data(world.getData(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ()));
    }

    @Override
    public BlockState getState() {
        return new DirectBlockState();
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
        return getBlockData().getAsString().equals("minecraft:air");
    }

    @Override
    public Location getLocation() {
        return pos.toLocation(world);
    }

    @Override
    public MaterialData getType() {
        return new Data(world.getData(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ()));
    }

    @Override
    public int getX() {
        return pos.getBlockX();
    }

    @Override
    public int getZ() {
        return pos.getBlockY();
    }

    @Override
    public int getY() {
        return pos.getBlockY();
    }

    @Override
    public boolean isPassable() {
        return false;
    }

    @Override
    public Object getHandle() {
        return world;
    }
}
