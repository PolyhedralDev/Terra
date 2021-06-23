package com.dfsek.terra.forge.block.data;

import com.dfsek.terra.api.block.BlockFace;
import com.dfsek.terra.api.block.data.Directional;
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
        switch(delegate.getValue(property)) {
            case SOUTH:
                return BlockFace.SOUTH;
            case DOWN:
                return BlockFace.DOWN;
            case UP:
                return BlockFace.UP;
            case EAST:
                return BlockFace.EAST;
            case WEST:
                return BlockFace.WEST;
            case NORTH:
                return BlockFace.NORTH;
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public void setFacing(BlockFace facing) {
        delegate = delegate.setValue(property, ForgeAdapter.adapt(facing));
    }
}
