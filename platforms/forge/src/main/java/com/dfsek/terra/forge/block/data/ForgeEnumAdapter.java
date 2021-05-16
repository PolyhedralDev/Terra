package com.dfsek.terra.forge.block.data;

import com.dfsek.terra.api.platform.block.Axis;
import com.dfsek.terra.api.platform.block.BlockFace;
import com.dfsek.terra.api.platform.block.data.Bisected;
import com.dfsek.terra.api.platform.block.data.Slab;
import com.dfsek.terra.api.platform.block.data.Stairs;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.SlabType;
import net.minecraft.state.properties.StairsShape;
import net.minecraft.util.Direction;

public final class ForgeEnumAdapter {
    public static Stairs.Shape adapt(StairsShape shape) {
        return switch(shape) {
            case OUTER_RIGHT -> Stairs.Shape.OUTER_RIGHT;
            case INNER_RIGHT -> Stairs.Shape.INNER_RIGHT;
            case OUTER_LEFT -> Stairs.Shape.OUTER_LEFT;
            case INNER_LEFT -> Stairs.Shape.INNER_LEFT;
            case STRAIGHT -> Stairs.Shape.STRAIGHT;
        };
    }

    public static Bisected.Half adapt(Half half) {
        return switch(half) {
            case BOTTOM -> Bisected.Half.BOTTOM;
            case TOP -> Bisected.Half.TOP;
        };
    }

    public static BlockFace adapt(Direction direction) {
        return switch(direction) {
            case DOWN -> BlockFace.DOWN;
            case UP -> BlockFace.UP;
            case WEST -> BlockFace.WEST;
            case EAST -> BlockFace.EAST;
            case NORTH -> BlockFace.NORTH;
            case SOUTH -> BlockFace.SOUTH;
        };
    }

    public static Slab.Type adapt(SlabType type) {
        return switch(type) {
            case BOTTOM -> Slab.Type.BOTTOM;
            case TOP -> Slab.Type.TOP;
            case DOUBLE -> Slab.Type.DOUBLE;
        };
    }

    public static StairsShape adapt(Stairs.Shape shape) {
        return switch(shape) {
            case STRAIGHT -> StairsShape.STRAIGHT;
            case INNER_LEFT -> StairsShape.INNER_LEFT;
            case OUTER_LEFT -> StairsShape.OUTER_LEFT;
            case INNER_RIGHT -> StairsShape.INNER_RIGHT;
            case OUTER_RIGHT -> StairsShape.OUTER_RIGHT;
        };
    }

    public static Half adapt(Bisected.Half half) {
        return switch(half) {
            case TOP -> Half.TOP;
            case BOTTOM -> Half.BOTTOM;
        };
    }

    public static Direction adapt(BlockFace face) {
        return switch(face) {
            case SOUTH -> Direction.SOUTH;
            case NORTH -> Direction.NORTH;
            case EAST -> Direction.EAST;
            case WEST -> Direction.WEST;
            case UP -> Direction.UP;
            case DOWN -> Direction.DOWN;
        };
    }

    public static SlabType adapt(Slab.Type type) {
        return switch(type) {
            case DOUBLE -> SlabType.DOUBLE;
            case TOP -> SlabType.TOP;
            case BOTTOM -> SlabType.BOTTOM;
        };
    }

    public static Axis adapt(Direction.Axis axis) {
        return switch(axis) {
            case X -> Axis.X;
            case Y -> Axis.Y;
            case Z -> Axis.Z;
        };
    }

    public static Direction.Axis adapt(Axis axis) {
        return switch(axis) {
            case Z -> Direction.Axis.Z;
            case Y -> Direction.Axis.Y;
            case X -> Direction.Axis.X;
        };
    }
}
