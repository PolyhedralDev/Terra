package com.dfsek.terra.fabric.util;

import com.dfsek.terra.api.block.data.properties.enums.Axis;
import com.dfsek.terra.api.block.BlockFace;
import com.dfsek.terra.api.block.data.Bisected;
import com.dfsek.terra.api.block.data.Slab;
import com.dfsek.terra.api.block.data.Stairs;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.fabric.block.FabricBlockState;
import com.dfsek.terra.fabric.block.data.FabricDirectional;
import com.dfsek.terra.fabric.block.data.FabricMultipleFacing;
import com.dfsek.terra.fabric.block.data.FabricOrientable;
import com.dfsek.terra.fabric.block.data.FabricRotatable;
import com.dfsek.terra.fabric.block.data.FabricSlab;
import com.dfsek.terra.fabric.block.data.FabricStairs;
import com.dfsek.terra.fabric.block.data.FabricWaterlogged;
import com.dfsek.terra.vector.Vector3Impl;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.SlabType;
import net.minecraft.block.enums.StairShape;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Arrays;

public final class FabricAdapter {
    public static BlockPos adapt(Vector3 v) {
        return new BlockPos(v.getBlockX(), v.getBlockY(), v.getBlockZ());
    }

    public static Vector3 adapt(BlockPos pos) {
        return new Vector3Impl(pos.getX(), pos.getY(), pos.getZ());
    }

    public static FabricBlockState adapt(BlockState state) {
        if(state.contains(Properties.STAIR_SHAPE)) return new FabricStairs(state);

        if(state.contains(Properties.SLAB_TYPE)) return new FabricSlab(state);

        if(state.contains(Properties.AXIS)) return new FabricOrientable(state, Properties.AXIS);
        if(state.contains(Properties.HORIZONTAL_AXIS)) return new FabricOrientable(state, Properties.HORIZONTAL_AXIS);

        if(state.contains(Properties.ROTATION)) return new FabricRotatable(state);

        if(state.contains(Properties.FACING)) return new FabricDirectional(state, Properties.FACING);
        if(state.contains(Properties.HOPPER_FACING)) return new FabricDirectional(state, Properties.HOPPER_FACING);
        if(state.contains(Properties.HORIZONTAL_FACING)) return new FabricDirectional(state, Properties.HORIZONTAL_FACING);

        if(state.getProperties().containsAll(Arrays.asList(Properties.NORTH, Properties.SOUTH, Properties.EAST, Properties.WEST)))
            return new FabricMultipleFacing(state);
        if(state.contains(Properties.WATERLOGGED)) return new FabricWaterlogged(state);
        return new FabricBlockState(state);
    }

    public static Direction adapt(BlockFace face) {
        switch(face) {
            case NORTH:
                return Direction.NORTH;
            case WEST:
                return Direction.WEST;
            case SOUTH:
                return Direction.SOUTH;
            case EAST:
                return Direction.EAST;
            case UP:
                return Direction.UP;
            case DOWN:
                return Direction.DOWN;
            default:
                throw new IllegalArgumentException("Illegal direction: " + face);
        }
    }

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
