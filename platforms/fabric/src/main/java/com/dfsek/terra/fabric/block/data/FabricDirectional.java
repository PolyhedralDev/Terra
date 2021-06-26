package com.dfsek.terra.fabric.block.data;

import com.dfsek.terra.api.block.BlockFace;
import com.dfsek.terra.api.block.data.Directional;
import com.dfsek.terra.fabric.block.FabricBlockState;
import com.dfsek.terra.fabric.util.FabricAdapter;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.DirectionProperty;

public class FabricDirectional extends FabricBlockState implements Directional {
    private final DirectionProperty property;

    public FabricDirectional(BlockState delegate, DirectionProperty property) {
        super(delegate);
        this.property = property;
    }

    @Override
    public BlockFace getFacing() {
        switch(delegate.get(property)) {
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
        delegate = delegate.with(property, FabricAdapter.adapt(facing));
    }
}
