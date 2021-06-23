package com.dfsek.terra.fabric.block.data;

import com.dfsek.terra.api.block.BlockFace;
import com.dfsek.terra.api.block.data.Stairs;
import com.dfsek.terra.fabric.util.FabricAdapter;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;

public class FabricStairs extends FabricWaterlogged implements Stairs {
    public FabricStairs(BlockState delegate) {
        super(delegate);
    }

    @Override
    public Shape getShape() {
        return FabricAdapter.adapt(getHandle().get(Properties.STAIR_SHAPE));
    }

    @Override
    public void setShape(Shape shape) {
        super.delegate = getHandle().with(Properties.STAIR_SHAPE, FabricAdapter.adapt(shape));
    }

    @Override
    public Half getHalf() {
        return FabricAdapter.adapt(getHandle().get(Properties.BLOCK_HALF));
    }

    @Override
    public void setHalf(Half half) {
        super.delegate = getHandle().with(Properties.BLOCK_HALF, FabricAdapter.adapt(half));
    }

    @Override
    public BlockFace getFacing() {
        return FabricAdapter.adapt(getHandle().get(Properties.HORIZONTAL_FACING));
    }

    @Override
    public void setFacing(BlockFace facing) {
        super.delegate = getHandle().with(Properties.HORIZONTAL_FACING, FabricAdapter.adapt(facing));
    }
}
