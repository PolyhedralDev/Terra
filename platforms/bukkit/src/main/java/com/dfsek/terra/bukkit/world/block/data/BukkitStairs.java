package com.dfsek.terra.bukkit.world.block.data;

import com.dfsek.terra.api.generic.world.block.BlockFace;
import com.dfsek.terra.api.generic.world.block.data.Stairs;
import com.dfsek.terra.bukkit.world.block.BukkitBlockData;

public class BukkitStairs extends BukkitBlockData implements Stairs {
    private final org.bukkit.block.data.type.Stairs stairs;

    public BukkitStairs(org.bukkit.block.data.type.Stairs delegate) {
        super(delegate);
        this.stairs = delegate;
    }

    @Override
    public Shape getShape() {
        return BukkitEnumAdapter.fromBukkitStair(stairs.getShape());
    }

    @Override
    public void setShape(Shape shape) {
        stairs.setShape(TerraEnumAdapter.fromTerraStair(shape));
    }

    @Override
    public Half getHalf() {
        return BukkitEnumAdapter.fromBukkitHalf(stairs.getHalf());
    }

    @Override
    public void setHalf(Half half) {
        stairs.setHalf(TerraEnumAdapter.fromTerraHalf(half));
    }

    @Override
    public BlockFace getFacing() {
        return BukkitEnumAdapter.fromBukkitBlockFace(stairs.getFacing());
    }

    @Override
    public void setFacing(BlockFace facing) {
        stairs.setFacing(TerraEnumAdapter.fromTerraBlockFace(facing));
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
