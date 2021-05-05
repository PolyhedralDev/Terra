package com.dfsek.terra.forge.block.data;

import com.dfsek.terra.api.platform.block.BlockFace;
import com.dfsek.terra.api.platform.block.data.Stairs;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;

public class ForgeStairs extends ForgeWaterlogged implements Stairs {
    public ForgeStairs(BlockState delegate) {
        super(delegate);
    }

    @Override
    public Shape getShape() {
        return ForgeEnumAdapter.adapt(getHandle().getValue(BlockStateProperties.STAIRS_SHAPE));
    }

    @Override
    public void setShape(Shape shape) {
        super.delegate = getHandle().setValue(BlockStateProperties.STAIRS_SHAPE, ForgeEnumAdapter.adapt(shape));
    }

    @Override
    public Half getHalf() {
        return ForgeEnumAdapter.adapt(getHandle().getValue(BlockStateProperties.HALF));
    }

    @Override
    public void setHalf(Half half) {
        super.delegate = getHandle().setValue(BlockStateProperties.HALF, ForgeEnumAdapter.adapt(half));
    }

    @Override
    public BlockFace getFacing() {
        return ForgeEnumAdapter.adapt(getHandle().getValue(BlockStateProperties.HORIZONTAL_FACING));
    }

    @Override
    public void setFacing(BlockFace facing) {
        super.delegate = getHandle().setValue(BlockStateProperties.HORIZONTAL_FACING, ForgeEnumAdapter.adapt(facing));
    }
}
