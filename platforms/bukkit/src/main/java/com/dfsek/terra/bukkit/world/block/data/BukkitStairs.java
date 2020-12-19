package com.dfsek.terra.bukkit.world.block.data;

import com.dfsek.terra.api.platform.world.block.BlockFace;
import com.dfsek.terra.api.platform.world.block.data.Stairs;
import com.dfsek.terra.bukkit.world.block.BukkitBlockData;

public class BukkitStairs extends BukkitBlockData implements Stairs {

    public BukkitStairs(org.bukkit.block.data.type.Stairs delegate) {
        super(delegate);
    }

    @Override
    public Shape getShape() {
        return BukkitEnumAdapter.fromBukkitStair(((org.bukkit.block.data.type.Stairs) super.getHandle()).getShape());
    }

    @Override
    public void setShape(Shape shape) {
        ((org.bukkit.block.data.type.Stairs) super.getHandle()).setShape(TerraEnumAdapter.fromTerraStair(shape));
    }

    @Override
    public Half getHalf() {
        return BukkitEnumAdapter.fromBukkitHalf(((org.bukkit.block.data.type.Stairs) super.getHandle()).getHalf());
    }

    @Override
    public void setHalf(Half half) {
        ((org.bukkit.block.data.type.Stairs) super.getHandle()).setHalf(TerraEnumAdapter.fromTerraHalf(half));
    }

    @Override
    public BlockFace getFacing() {
        return BukkitEnumAdapter.fromBukkitBlockFace(((org.bukkit.block.data.type.Stairs) super.getHandle()).getFacing());
    }

    @Override
    public void setFacing(BlockFace facing) {
        ((org.bukkit.block.data.type.Stairs) super.getHandle()).setFacing(TerraEnumAdapter.fromTerraBlockFace(facing));
    }

    @Override
    public boolean isWaterlogged() {
        return ((org.bukkit.block.data.type.Stairs) super.getHandle()).isWaterlogged();
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        ((org.bukkit.block.data.type.Stairs) super.getHandle()).setWaterlogged(waterlogged);
    }
}
