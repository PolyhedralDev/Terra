package com.dfsek.terra.forge.block.data;

import com.dfsek.terra.api.platform.block.BlockFace;
import com.dfsek.terra.api.platform.block.data.Directional;
import com.dfsek.terra.forge.ForgeAdapter;
import com.dfsek.terra.forge.block.ForgeBlockData;
import net.minecraft.block.BlockState;
import net.minecraft.state.DirectionProperty;

public class ForgeDirectional extends ForgeBlockData implements Directional {
    private final DirectionProperty property;

    public ForgeDirectional(BlockState delegate, DirectionProperty property) {
        super(delegate);
        this.property = property;
    }

    @Override
    public BlockFace getFacing() {
        return switch(delegate.getValue(property)) {
            case SOUTH -> BlockFace.SOUTH;
            case DOWN -> BlockFace.DOWN;
            case UP -> BlockFace.UP;
            case EAST -> BlockFace.EAST;
            case WEST -> BlockFace.WEST;
            case NORTH -> BlockFace.NORTH;
        };
    }

    @Override
    public void setFacing(BlockFace facing) {
        delegate = delegate.setValue(property, ForgeAdapter.adapt(facing));
    }
}
