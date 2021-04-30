package com.dfsek.terra.forge.world.block.data;

import com.dfsek.terra.api.platform.block.BlockFace;
import com.dfsek.terra.api.platform.block.data.Rotatable;
import com.dfsek.terra.forge.world.block.ForgeBlockData;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;

public class ForgeRotatable extends ForgeBlockData implements Rotatable {
    public ForgeRotatable(BlockState delegate) {
        super(delegate);
    }

    @Override
    public BlockFace getRotation() {
        int r = delegate.getValue(BlockStateProperties.ROTATION_16);
        switch(r) {
            case 0:
                return BlockFace.SOUTH;
            case 1:
                return BlockFace.SOUTH_SOUTH_WEST;
            case 2:
                return BlockFace.SOUTH_WEST;
            case 3:
                return BlockFace.WEST_SOUTH_WEST;
            case 4:
                return BlockFace.WEST;
            case 5:
                return BlockFace.WEST_NORTH_WEST;
            case 6:
                return BlockFace.NORTH_WEST;
            case 7:
                return BlockFace.NORTH_NORTH_WEST;
            case 8:
                return BlockFace.NORTH;
            case 9:
                return BlockFace.NORTH_NORTH_EAST;
            case 10:
                return BlockFace.NORTH_EAST;
            case 11:
                return BlockFace.EAST_NORTH_EAST;
            case 12:
                return BlockFace.EAST;
            case 13:
                return BlockFace.EAST_SOUTH_EAST;
            case 14:
                return BlockFace.SOUTH_EAST;
            case 15:
                return BlockFace.SOUTH_SOUTH_EAST;
            default:
                throw new IllegalArgumentException("Unknown rotation " + r);
        }
    }

    @Override
    public void setRotation(BlockFace face) {
        switch(face) {
            case UP:
            case DOWN:
                throw new IllegalArgumentException("Illegal rotation " + face);
            case SOUTH:
                delegate = delegate.setValue(BlockStateProperties.ROTATION_16, 0);
                return;
            case SOUTH_SOUTH_WEST:
                delegate = delegate.setValue(BlockStateProperties.ROTATION_16, 1);
                return;
            case SOUTH_WEST:
                delegate = delegate.setValue(BlockStateProperties.ROTATION_16, 2);
                return;
            case WEST_SOUTH_WEST:
                delegate = delegate.setValue(BlockStateProperties.ROTATION_16, 3);
                return;
            case WEST:
                delegate = delegate.setValue(BlockStateProperties.ROTATION_16, 4);
                return;
            case WEST_NORTH_WEST:
                delegate = delegate.setValue(BlockStateProperties.ROTATION_16, 5);
                return;
            case NORTH_WEST:
                delegate = delegate.setValue(BlockStateProperties.ROTATION_16, 6);
                return;
            case NORTH_NORTH_WEST:
                delegate = delegate.setValue(BlockStateProperties.ROTATION_16, 7);
                return;
            case NORTH:
                delegate = delegate.setValue(BlockStateProperties.ROTATION_16, 8);
                return;
            case NORTH_NORTH_EAST:
                delegate = delegate.setValue(BlockStateProperties.ROTATION_16, 9);
                return;
            case NORTH_EAST:
                delegate = delegate.setValue(BlockStateProperties.ROTATION_16, 10);
                return;
            case EAST_NORTH_EAST:
                delegate = delegate.setValue(BlockStateProperties.ROTATION_16, 11);
                return;
            case EAST:
                delegate = delegate.setValue(BlockStateProperties.ROTATION_16, 12);
                return;
            case EAST_SOUTH_EAST:
                delegate = delegate.setValue(BlockStateProperties.ROTATION_16, 13);
                return;
            case SOUTH_EAST:
                delegate = delegate.setValue(BlockStateProperties.ROTATION_16, 14);
                return;
            case SOUTH_SOUTH_EAST:
                delegate = delegate.setValue(BlockStateProperties.ROTATION_16, 15);
                return;
        }
    }
}
