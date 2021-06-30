package com.dfsek.terra.fabric.util;

import com.dfsek.terra.api.block.state.properties.enums.Half;
import com.dfsek.terra.api.block.state.properties.enums.Axis;
import com.dfsek.terra.api.block.state.properties.enums.RailShape;
import com.dfsek.terra.api.block.state.properties.enums.RedstoneConnection;
import com.dfsek.terra.api.block.state.properties.enums.WallHeight;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.fabric.block.FabricBlockState;
import com.dfsek.terra.vector.Vector3Impl;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.WallShape;
import net.minecraft.block.enums.WireConnection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public final class FabricAdapter {
    public static BlockPos adapt(Vector3 v) {
        return new BlockPos(v.getBlockX(), v.getBlockY(), v.getBlockZ());
    }

    public static Vector3 adapt(BlockPos pos) {
        return new Vector3Impl(pos.getX(), pos.getY(), pos.getZ());
    }

    public static FabricBlockState adapt(BlockState state) {
        return new FabricBlockState(state);
    }

    public static Direction adapt(com.dfsek.terra.api.block.state.properties.enums.Direction direction) {
        switch(direction) {
            case SOUTH:
                return Direction.SOUTH;
            case NORTH:
                return Direction.NORTH;
            case WEST:
                return Direction.WEST;
            case EAST:
                return Direction.EAST;
            case UP:
                return Direction.UP;
            case DOWN:
                return Direction.DOWN;
        }
        throw new IllegalArgumentException();
    }

    public static com.dfsek.terra.api.block.state.properties.enums.Direction adapt(Direction direction) {
        switch(direction) {
            case SOUTH:
                return com.dfsek.terra.api.block.state.properties.enums.Direction.SOUTH;
            case NORTH:
                return com.dfsek.terra.api.block.state.properties.enums.Direction.NORTH;
            case WEST:
                return com.dfsek.terra.api.block.state.properties.enums.Direction.WEST;
            case EAST:
                return com.dfsek.terra.api.block.state.properties.enums.Direction.EAST;
            case UP:
                return com.dfsek.terra.api.block.state.properties.enums.Direction.UP;
            case DOWN:
                return com.dfsek.terra.api.block.state.properties.enums.Direction.DOWN;
        }
        throw new IllegalArgumentException();
    }

    public static WallHeight adapt(WallShape shape) {
        switch(shape) {
            case LOW:
                return WallHeight.LOW;
            case NONE:
                return WallHeight.NONE;
            case TALL:
                return WallHeight.TALL;
        }
        throw new IllegalArgumentException();
    }

    public static WallShape adapt(WallHeight shape) {
        switch(shape) {
            case LOW:
                return WallShape.LOW;
            case NONE:
                return WallShape.NONE;
            case TALL:
                return WallShape.TALL;
        }
        throw new IllegalArgumentException();
    }

    public static RedstoneConnection adapt(WireConnection connection) {
        switch(connection) {
            case NONE:
                return RedstoneConnection.NONE;
            case UP:
                return RedstoneConnection.UP;
            case SIDE:
                return RedstoneConnection.SIDE;
        }
        throw new IllegalArgumentException();
    }

    public static WireConnection adapt(RedstoneConnection connection) {
        switch(connection) {
            case NONE:
                return WireConnection.NONE;
            case UP:
                return WireConnection.UP;
            case SIDE:
                return WireConnection.SIDE;
        }
        throw new IllegalArgumentException();
    }


    public static Half adapt(BlockHalf half) {
        switch(half) {
            case BOTTOM:
                return Half.BOTTOM;
            case TOP:
                return Half.TOP;
            default:
                throw new IllegalStateException();
        }
    }

    public static BlockHalf adapt(Half half) {
        switch(half) {
            case TOP:
                return BlockHalf.TOP;
            case BOTTOM:
                return BlockHalf.BOTTOM;
            default:
                throw new IllegalStateException();
        }
    }

    public static RailShape adapt(net.minecraft.block.enums.RailShape railShape) {
        switch(railShape) {
            case EAST_WEST:
                return RailShape.EAST_WEST;
            case NORTH_EAST:
                return RailShape.NORTH_EAST;
            case NORTH_WEST:
                return RailShape.NORTH_WEST;
            case SOUTH_EAST:
                return RailShape.SOUTH_EAST;
            case SOUTH_WEST:
                return RailShape.SOUTH_WEST;
            case NORTH_SOUTH:
                return RailShape.NORTH_SOUTH;
            case ASCENDING_EAST:
                return RailShape.ASCENDING_EAST;
            case ASCENDING_NORTH:
                return RailShape.ASCENDING_NORTH;
            case ASCENDING_SOUTH:
                return RailShape.ASCENDING_SOUTH;
            case ASCENDING_WEST:
                return RailShape.ASCENDING_WEST;
        }
        throw new IllegalStateException();
    }

    public static net.minecraft.block.enums.RailShape adapt(RailShape railShape) {
        switch(railShape) {
            case EAST_WEST:
                return net.minecraft.block.enums.RailShape.EAST_WEST;
            case NORTH_EAST:
                return net.minecraft.block.enums.RailShape.NORTH_EAST;
            case NORTH_WEST:
                return net.minecraft.block.enums.RailShape.NORTH_WEST;
            case SOUTH_EAST:
                return net.minecraft.block.enums.RailShape.SOUTH_EAST;
            case SOUTH_WEST:
                return net.minecraft.block.enums.RailShape.SOUTH_WEST;
            case NORTH_SOUTH:
                return net.minecraft.block.enums.RailShape.NORTH_SOUTH;
            case ASCENDING_EAST:
                return net.minecraft.block.enums.RailShape.ASCENDING_EAST;
            case ASCENDING_NORTH:
                return net.minecraft.block.enums.RailShape.ASCENDING_NORTH;
            case ASCENDING_SOUTH:
                return net.minecraft.block.enums.RailShape.ASCENDING_SOUTH;
            case ASCENDING_WEST:
                return net.minecraft.block.enums.RailShape.ASCENDING_WEST;
        }
        throw new IllegalStateException();
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
