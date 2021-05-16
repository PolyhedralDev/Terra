package com.dfsek.terra.fabric.block.data;

import com.dfsek.terra.api.platform.block.BlockFace;
import com.dfsek.terra.api.platform.block.data.Directional;
import com.dfsek.terra.fabric.block.FabricBlockData;
import com.dfsek.terra.fabric.util.FabricAdapter;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.DirectionProperty;

public class FabricDirectional extends FabricBlockData implements Directional {
    private final DirectionProperty property;

    public FabricDirectional(BlockState delegate, DirectionProperty property) {
        super(delegate);
        this.property = property;
    }

    @Override
    public BlockFace getFacing() {
        return switch(delegate.get(property)) {
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
        delegate = delegate.with(property, FabricAdapter.adapt(facing));
    }
}
