package com.dfsek.terra.api.bukkit.world.block.data;

import com.dfsek.terra.api.bukkit.world.block.BukkitBlockData;
import com.dfsek.terra.api.generic.world.block.BlockFace;
import com.dfsek.terra.api.generic.world.block.data.Stairs;
import org.bukkit.block.data.BlockData;

public class BukkitStairs extends BukkitBlockData implements Stairs {
    private final org.bukkit.block.data.type.Stairs stairs;

    public BukkitStairs(BlockData delegate) {
        super(delegate);
        this.stairs = (org.bukkit.block.data.type.Stairs) delegate;
    }

    @Override
    public Shape getShape() {
        return BukkitEnumAdapter.fromBukkitStair(stairs.getShape());
    }

    @Override
    public void setShape(Shape shape) {
        stairs.setShape(BukkitEnumAdapter.fromTerraStair(shape));
    }

    @Override
    public Half getHalf() {
        return BukkitEnumAdapter.fromBukkitHalf(stairs.getHalf());
    }

    @Override
    public void setHalf(Half half) {
        stairs.setHalf(BukkitEnumAdapter.fromTerraHalf(half));
    }

    @Override
    public BlockFace getFacing() {
        return BukkitEnumAdapter.fromBukkitBlockFace(stairs.getFacing());
    }

    @Override
    public void setFacing(BlockFace facing) {
        stairs.setFacing(BukkitEnumAdapter.fromTerraBlockFace(facing));
    }

    @Override
    public boolean isWaterlogged() {
        return stairs.isWaterlogged();
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        stairs.setWaterlogged(waterlogged);
    }
}
