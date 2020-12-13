package com.dfsek.terra.bukkit.world.block.data;


import com.dfsek.terra.api.generic.world.block.BlockFace;
import com.dfsek.terra.api.generic.world.block.data.Bisected;
import com.dfsek.terra.api.generic.world.block.data.Stairs;

/**
 * Utility class to adapt Bukkit enums to Terra enums.
 */
public final class BukkitEnumAdapter {
    public static Stairs.Shape fromBukkitStair(org.bukkit.block.data.type.Stairs.Shape shape) {
        switch(shape) {
            case STRAIGHT:
                return Stairs.Shape.STRAIGHT;
            case INNER_LEFT:
                return Stairs.Shape.INNER_LEFT;
            case OUTER_LEFT:
                return Stairs.Shape.OUTER_LEFT;
            case INNER_RIGHT:
                return Stairs.Shape.INNER_RIGHT;
            case OUTER_RIGHT:
                return Stairs.Shape.OUTER_RIGHT;
            default:
                throw new IllegalStateException();
        }
    }

    public static Bisected.Half fromBukkitHalf(org.bukkit.block.data.Bisected.Half half) {
        switch(half) {
            case BOTTOM:
                return Bisected.Half.BOTTOM;
            case TOP:
                return Bisected.Half.TOP;
            default:
                throw new IllegalStateException();
        }
    }

    public static BlockFace fromBukkitBlockFace(org.bukkit.block.BlockFace face) {
        switch(face) {
            case DOWN:
                return BlockFace.DOWN;
            case UP:
                return BlockFace.UP;
            case NORTH_WEST:
                return BlockFace.NORTH_WEST;
            case NORTH_EAST:
                return BlockFace.NORTH_EAST;
            case SOUTH_EAST:
                return BlockFace.SOUTH_EAST;
            case SOUTH_WEST:
                return BlockFace.SOUTH_WEST;
            case NORTH_NORTH_WEST:
                return BlockFace.NORTH_NORTH_WEST;
            case WEST_NORTH_WEST:
                return BlockFace.WEST_NORTH_WEST;
            case WEST_SOUTH_WEST:
                return BlockFace.WEST_SOUTH_WEST;
            case SOUTH_SOUTH_WEST:
                return BlockFace.SOUTH_SOUTH_WEST;
            case EAST_NORTH_EAST:
                return BlockFace.EAST_NORTH_EAST;
            case WEST:
                return BlockFace.WEST;
            case SOUTH:
                return BlockFace.SOUTH;
            case EAST:
                return BlockFace.EAST;
            case NORTH:
                return BlockFace.NORTH;
            case SELF:
                return BlockFace.SELF;
            case EAST_SOUTH_EAST:
                return BlockFace.EAST_SOUTH_EAST;
            case NORTH_NORTH_EAST:
                return BlockFace.NORTH_NORTH_EAST;
            case SOUTH_SOUTH_EAST:
                return BlockFace.SOUTH_SOUTH_EAST;
            default:
                throw new IllegalStateException();
        }
    }

}
