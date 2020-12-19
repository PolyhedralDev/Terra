package com.dfsek.terra.fabric.world.block.data;

import com.dfsek.terra.api.platform.world.block.BlockFace;
import com.dfsek.terra.api.platform.world.block.data.Bisected;
import com.dfsek.terra.api.platform.world.block.data.Slab;
import com.dfsek.terra.api.platform.world.block.data.Stairs;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.SlabType;
import net.minecraft.block.enums.StairShape;
import net.minecraft.util.math.Direction;

public final class TerraEnumAdapter {
    public static StairShape fromTerraStairShape(Stairs.Shape shape) {
        switch(shape) {
            case STRAIGHT:
                return StairShape.STRAIGHT;
            case INNER_LEFT:
                return StairShape.INNER_LEFT;
            case OUTER_LEFT:
                return StairShape.OUTER_LEFT;
            case INNER_RIGHT:
                return StairShape.INNER_RIGHT;
            case OUTER_RIGHT:
                return StairShape.OUTER_RIGHT;
            default:
                throw new IllegalStateException();
        }
    }

    public static BlockHalf fromTerraHalf(Bisected.Half half) {
        switch(half) {
            case TOP:
                return BlockHalf.TOP;
            case BOTTOM:
                return BlockHalf.BOTTOM;
            default:
                throw new IllegalStateException();
        }
    }

    public static Direction fromTerraBlockFace(BlockFace face) {
        switch(face) {
            case SOUTH:
                return Direction.SOUTH;
            case NORTH:
                return Direction.NORTH;
            case EAST:
                return Direction.EAST;
            case WEST:
                return Direction.WEST;
            case UP:
                return Direction.UP;
            case DOWN:
                return Direction.DOWN;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static SlabType fromTerraSlabType(Slab.Type type) {
        switch(type) {
            case DOUBLE:
                return SlabType.DOUBLE;
            case TOP:
                return SlabType.TOP;
            case BOTTOM:
                return SlabType.BOTTOM;
            default:
                throw new IllegalStateException();
        }
    }
}
