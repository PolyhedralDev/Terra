package com.dfsek.terra.forge.block.data;

import com.dfsek.terra.api.platform.block.BlockFace;
import com.dfsek.terra.api.platform.block.data.Rotatable;
import com.dfsek.terra.forge.block.ForgeBlockData;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;

public class ForgeRotatable extends ForgeBlockData implements Rotatable {
    public ForgeRotatable(BlockState delegate) {
        super(delegate);
    }

    @Override
    public BlockFace getRotation() {
        int r = delegate.getValue(BlockStateProperties.ROTATION_16);
        return switch(r) {
            case 0 -> BlockFace.SOUTH;
            case 1 -> BlockFace.SOUTH_SOUTH_WEST;
            case 2 -> BlockFace.SOUTH_WEST;
            case 3 -> BlockFace.WEST_SOUTH_WEST;
            case 4 -> BlockFace.WEST;
            case 5 -> BlockFace.WEST_NORTH_WEST;
            case 6 -> BlockFace.NORTH_WEST;
            case 7 -> BlockFace.NORTH_NORTH_WEST;
            case 8 -> BlockFace.NORTH;
            case 9 -> BlockFace.NORTH_NORTH_EAST;
            case 10 -> BlockFace.NORTH_EAST;
            case 11 -> BlockFace.EAST_NORTH_EAST;
            case 12 -> BlockFace.EAST;
            case 13 -> BlockFace.EAST_SOUTH_EAST;
            case 14 -> BlockFace.SOUTH_EAST;
            case 15 -> BlockFace.SOUTH_SOUTH_EAST;
            default -> throw new IllegalArgumentException("Unknown rotation " + r);
        };
    }

    @Override
    public void setRotation(BlockFace face) {
        switch(face) {
            case UP, DOWN -> throw new IllegalArgumentException("Illegal rotation " + face);
            case SOUTH -> delegate = delegate.setValue(BlockStateProperties.ROTATION_16, 0);
            case SOUTH_SOUTH_WEST -> delegate = delegate.setValue(BlockStateProperties.ROTATION_16, 1);
            case SOUTH_WEST -> delegate = delegate.setValue(BlockStateProperties.ROTATION_16, 2);
            case WEST_SOUTH_WEST -> delegate = delegate.setValue(BlockStateProperties.ROTATION_16, 3);
            case WEST -> delegate = delegate.setValue(BlockStateProperties.ROTATION_16, 4);
            case WEST_NORTH_WEST -> delegate = delegate.setValue(BlockStateProperties.ROTATION_16, 5);
            case NORTH_WEST -> delegate = delegate.setValue(BlockStateProperties.ROTATION_16, 6);
            case NORTH_NORTH_WEST -> delegate = delegate.setValue(BlockStateProperties.ROTATION_16, 7);
            case NORTH -> delegate = delegate.setValue(BlockStateProperties.ROTATION_16, 8);
            case NORTH_NORTH_EAST -> delegate = delegate.setValue(BlockStateProperties.ROTATION_16, 9);
            case NORTH_EAST -> delegate = delegate.setValue(BlockStateProperties.ROTATION_16, 10);
            case EAST_NORTH_EAST -> delegate = delegate.setValue(BlockStateProperties.ROTATION_16, 11);
            case EAST -> delegate = delegate.setValue(BlockStateProperties.ROTATION_16, 12);
            case EAST_SOUTH_EAST -> delegate = delegate.setValue(BlockStateProperties.ROTATION_16, 13);
            case SOUTH_EAST -> delegate = delegate.setValue(BlockStateProperties.ROTATION_16, 14);
            case SOUTH_SOUTH_EAST -> delegate = delegate.setValue(BlockStateProperties.ROTATION_16, 15);
        }
    }
}
