package com.dfsek.terra.fabric.block.data;

import com.dfsek.terra.api.platform.block.Axis;
import com.dfsek.terra.api.platform.block.BlockFace;
import com.dfsek.terra.api.platform.block.data.Bisected;
import com.dfsek.terra.api.platform.block.data.Slab;
import com.dfsek.terra.api.platform.block.data.Stairs;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.SlabType;
import net.minecraft.block.enums.StairShape;
import net.minecraft.util.math.Direction;

public final class FabricEnumAdapter {
    public static Stairs.Shape adapt(StairShape shape) {
        switch(shape) {
            case OUTER_RIGHT:
                return Stairs.Shape.OUTER_RIGHT;
            case INNER_RIGHT:
                return Stairs.Shape.INNER_RIGHT;
            case OUTER_LEFT:
                return Stairs.Shape.OUTER_LEFT;
            case INNER_LEFT:
                return Stairs.Shape.INNER_LEFT;
            case STRAIGHT:
                return Stairs.Shape.STRAIGHT;
            default:
                throw new IllegalStateException();
        }
    }

    public static Bisected.Half adapt(BlockHalf half) {
        switch(half) {
            case BOTTOM:
                return Bisected.Half.BOTTOM;
            case TOP:
                return Bisected.Half.TOP;
            default:
                throw new IllegalStateException();
        }
    }

    public static BlockFace adapt(Direction direction) {
        switch(direction) {
            case DOWN:
                return BlockFace.DOWN;
            case UP:
                return BlockFace.UP;
            case WEST:
                return BlockFace.WEST;
            case EAST:
                return BlockFace.EAST;
            case NORTH:
                return BlockFace.NORTH;
            case SOUTH:
                return BlockFace.SOUTH;
            default:
                throw new IllegalStateException();
        }
    }

    public static Slab.Type adapt(SlabType type) {
        switch(type) {
            case BOTTOM:
                return Slab.Type.BOTTOM;
            case TOP:
                return Slab.Type.TOP;
            case DOUBLE:
                return Slab.Type.DOUBLE;
            default:
                throw new IllegalStateException();
        }
    }

    public static StairShape adapt(Stairs.Shape shape) {
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

    public static BlockHalf adapt(Bisected.Half half) {
        switch(half) {
            case TOP:
                return BlockHalf.TOP;
            case BOTTOM:
                return BlockHalf.BOTTOM;
            default:
                throw new IllegalStateException();
        }
    }

    public static Direction adapt(BlockFace face) {
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

    public static SlabType adapt(Slab.Type type) {
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

    public static Axis adapt(Direction.Axis axis) {
        switch(axis) {
            case X:
                return Axis.X;
            case Y:
                return Axis.Y;
            case Z:
                return Axis.Z;
            default:
                throw new IllegalStateException();
        }
    }

    public static Direction.Axis adapt(Axis axis) {
        switch(axis) {
            case Z:
                return Direction.Axis.Z;
            case Y:
                return Direction.Axis.Y;
            case X:
                return Direction.Axis.X;
            default:
                throw new IllegalStateException();
        }
    }
}
