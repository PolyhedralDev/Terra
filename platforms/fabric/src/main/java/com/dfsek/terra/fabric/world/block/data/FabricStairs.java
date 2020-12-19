package com.dfsek.terra.fabric.world.block.data;

import com.dfsek.terra.api.platform.block.BlockFace;
import com.dfsek.terra.api.platform.block.data.Stairs;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;

public class FabricStairs extends FabricWaterlogged implements Stairs {
    public FabricStairs(BlockState delegate) {
        super(delegate);
    }

    @Override
    public Shape getShape() {
        return FabricEnumAdapter.fromFabricStairShape(getHandle().get(Properties.STAIR_SHAPE));
    }

    @Override
    public void setShape(Shape shape) {
        super.delegate = getHandle().with(Properties.STAIR_SHAPE, TerraEnumAdapter.fromTerraStairShape(shape));
    }

    @Override
    public Half getHalf() {
        return FabricEnumAdapter.fromFabricHalf(getHandle().get(Properties.BLOCK_HALF));
    }

    @Override
    public void setHalf(Half half) {
        super.delegate = getHandle().with(Properties.BLOCK_HALF, TerraEnumAdapter.fromTerraHalf(half));
    }

    @Override
    public BlockFace getFacing() {
        return FabricEnumAdapter.fromFabricDirection(getHandle().get(Properties.HORIZONTAL_FACING));
    }

    @Override
    public void setFacing(BlockFace facing) {
        super.delegate = getHandle().with(Properties.HORIZONTAL_FACING, TerraEnumAdapter.fromTerraBlockFace(facing));
    }
}
