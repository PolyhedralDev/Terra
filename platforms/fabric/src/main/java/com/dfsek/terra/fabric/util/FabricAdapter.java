package com.dfsek.terra.fabric.util;

import com.dfsek.terra.api.block.state.properties.enums.Half;
import com.dfsek.terra.api.block.state.properties.enums.Axis;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.fabric.block.FabricBlockState;
import com.dfsek.terra.vector.Vector3Impl;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.BlockHalf;
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
