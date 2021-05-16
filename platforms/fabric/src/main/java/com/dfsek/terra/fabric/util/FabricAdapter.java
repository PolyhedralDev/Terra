package com.dfsek.terra.fabric.util;

import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.platform.block.Axis;
import com.dfsek.terra.api.platform.block.BlockFace;
import com.dfsek.terra.api.platform.block.data.Bisected;
import com.dfsek.terra.api.platform.block.data.Slab;
import com.dfsek.terra.api.platform.block.data.Stairs;
import com.dfsek.terra.api.platform.block.state.Container;
import com.dfsek.terra.api.platform.block.state.MobSpawner;
import com.dfsek.terra.api.platform.block.state.Sign;
import com.dfsek.terra.fabric.block.FabricBlockData;
import com.dfsek.terra.fabric.block.data.FabricDirectional;
import com.dfsek.terra.fabric.block.data.FabricMultipleFacing;
import com.dfsek.terra.fabric.block.data.FabricOrientable;
import com.dfsek.terra.fabric.block.data.FabricRotatable;
import com.dfsek.terra.fabric.block.data.FabricSlab;
import com.dfsek.terra.fabric.block.data.FabricStairs;
import com.dfsek.terra.fabric.block.data.FabricWaterlogged;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.SlabType;
import net.minecraft.block.enums.StairShape;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

import java.util.Arrays;

public final class FabricAdapter {
    public static BlockPos adapt(Vector3 v) {
        return new BlockPos(v.getBlockX(), v.getBlockY(), v.getBlockZ());
    }

    public static Vector3 adapt(BlockPos pos) {
        return new Vector3(pos.getX(), pos.getY(), pos.getZ());
    }

    public static FabricBlockData adapt(BlockState state) {
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
        return new FabricBlockData(state);
    }

    public static Direction adapt(BlockFace face) {
        return switch(face) {
            case NORTH -> Direction.NORTH;
            case WEST -> Direction.WEST;
            case SOUTH -> Direction.SOUTH;
            case EAST -> Direction.EAST;
            case UP -> Direction.UP;
            case DOWN -> Direction.DOWN;
            default -> throw new IllegalArgumentException("Illegal direction: " + face);
        };
    }


    public static com.dfsek.terra.api.platform.block.state.BlockState adapt(com.dfsek.terra.api.platform.block.Block block) {
        WorldAccess worldAccess = (WorldAccess) block.getLocation().getWorld();

        BlockEntity entity = worldAccess.getBlockEntity(adapt(block.getLocation().toVector()));
        if(entity instanceof SignBlockEntity) {
            return (Sign) entity;
        } else if(entity instanceof MobSpawnerBlockEntity) {
            return (MobSpawner) entity;
        } else if(entity instanceof LootableContainerBlockEntity) {
            return (Container) entity;
        }
        return null;
    }

    public static Stairs.Shape adapt(StairShape shape) {
        return switch(shape) {
            case OUTER_RIGHT -> Stairs.Shape.OUTER_RIGHT;
            case INNER_RIGHT -> Stairs.Shape.INNER_RIGHT;
            case OUTER_LEFT -> Stairs.Shape.OUTER_LEFT;
            case INNER_LEFT -> Stairs.Shape.INNER_LEFT;
            case STRAIGHT -> Stairs.Shape.STRAIGHT;
        };
    }

    public static Bisected.Half adapt(BlockHalf half) {
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

    public static StairShape adapt(Stairs.Shape shape) {
        return switch(shape) {
            case STRAIGHT -> StairShape.STRAIGHT;
            case INNER_LEFT -> StairShape.INNER_LEFT;
            case OUTER_LEFT -> StairShape.OUTER_LEFT;
            case INNER_RIGHT -> StairShape.INNER_RIGHT;
            case OUTER_RIGHT -> StairShape.OUTER_RIGHT;
        };
    }

    public static BlockHalf adapt(Bisected.Half half) {
        return switch(half) {
            case TOP -> BlockHalf.TOP;
            case BOTTOM -> BlockHalf.BOTTOM;
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
