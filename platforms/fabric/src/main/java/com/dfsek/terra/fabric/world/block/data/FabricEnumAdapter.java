package com.dfsek.terra.fabric.world.block.data;

import com.dfsek.terra.api.platform.block.BlockFace;
import com.dfsek.terra.api.platform.block.data.Bisected;
import com.dfsek.terra.api.platform.block.data.Slab;
import com.dfsek.terra.api.platform.block.data.Stairs;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.SlabType;
import net.minecraft.block.enums.StairShape;
import net.minecraft.util.math.Direction;

public final class FabricEnumAdapter {
    public static Stairs.Shape fromFabricStairShape(StairShape shape) {
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

    public static Bisected.Half fromFabricHalf(BlockHalf half) {
        switch(half) {
            case BOTTOM:
                return Bisected.Half.BOTTOM;
            case TOP:
                return Bisected.Half.TOP;
            default:
                throw new IllegalStateException();
        }
    }

    public static BlockFace fromFabricDirection(Direction direction) {
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

    public static Slab.Type fromFabricSlabType(SlabType type) {
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
}
